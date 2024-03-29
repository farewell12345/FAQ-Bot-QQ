package ink.umb.faqbot.route

import ink.umb.faqbot.AppConfig
import ink.umb.faqbot.bot.manager.BotsManager
import ink.umb.faqbot.dto.db.logger
import kotlinx.coroutines.*
import net.mamoe.mirai.contact.PermissionDeniedException
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.findIsInstance
import kotlin.coroutines.CoroutineContext

val unCompleteValue = hashMapOf<Long, CompletableDeferred<String>>()

class CommandRoute<T : MessageEvent>(val args: List<String>?, val event: T) : CoroutineScope {
    val helpMap = hashMapOf<String, String>()
    var alreadyCalled = false
    lateinit var commandText:String
    private val job = Job()
    private val logger = logger()
    private var errHandler: (suspend (Throwable) -> Message?)? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    suspend inline fun case(
        case: String,
        desc: String = "暂无描述",
        canRepetition: Boolean = true,
        receiver: T.(List<String>?) -> Unit
    ) {
        this.commandText = event.message
            .findIsInstance<PlainText>()?.content
            ?.replace(case,"")
            ?.replace(" ","").toString()
        synchronized(helpMap) {
            helpMap[case] = desc
        }
        if (args?.size == 0) {
            return
        }

        if (!alreadyCalled && args?.find { it.replace(" ", "").equals(case) } != null) {
            if (!BotsManager.task.canUseBot(event.sender.id) && canRepetition) {
                event.subject.sendMessage(PlainText("技能冷却中（你发这么快急着投胎嘛，要不我送你一程）"))
                return
            }
            alreadyCalled = true
            BotsManager.task.flushUser(event.sender.id)
            kotlin.runCatching {
                try {
                    event.receiver(args.subList(1, args.size))
                }catch (e:Exception){
                    val subject = event.subject
                    subject.sendMessage(e.message!!)
                }
            }.also {
                handleException(it.exceptionOrNull())
            }
        }
    }

    suspend inline fun furry(case: String, desc: String = "暂无描述", receiver: T.(List<String>?) -> Unit) {
        helpMap[case] = desc
        if (args?.size == 0) {
            return
        }
        try {
            val pattern = Regex(args.toString())
            if (!alreadyCalled && pattern.find(case) != null) {
                alreadyCalled = true
                kotlin.runCatching { event.receiver(args?.subList(1, args.size)) }.also {
                    handleException(it.exceptionOrNull())
                }
            }
        } catch (e: Exception) {
            logger().info(e)
        }
    }

    suspend fun handleException(throwable: Throwable?) {
        logger.error(throwable ?: return)
        val repMsg = errHandler?.let { it1 -> it1(throwable) }
        event.subject.sendMessage(repMsg ?: return)
    }

    fun getHelp(): String = buildString {
        append("指令帮助：\n")
        helpMap.forEach { s, s2 ->
            append("${s}:${s2}\n")
        }
    }
}

suspend inline fun <reified T : MessageEvent> T.route(
    prefix: String = "",
    delimiter: String = " ",
    crossinline receiver: suspend CommandRoute<T>.() -> Unit
): Boolean {
    return try {
        val msg = this.message.contentToString()
        if (unCompleteValue.containsKey(this.sender.id)) {
            unCompleteValue[this.sender.id]?.complete(msg)
        }
        if (!msg.startsWith(prefix)) {
            return false
        }
        val args = msg.split(delimiter)
        val router = CommandRoute(args, this)
        router.launch { receiver(router) }
        true
    } catch (e: PermissionDeniedException) {
        true
    } catch (e: Exception) {
        val subject = this.subject
        kotlin.runCatching {
            subject.sendMessage(e.message!!)
        }
        false
    }
}