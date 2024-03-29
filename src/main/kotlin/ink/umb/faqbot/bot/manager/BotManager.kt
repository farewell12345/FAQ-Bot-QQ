@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package ink.umb.faqbot.bot.manager
import ink.umb.faqbot.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext
internal val appJob = Job()

object CommandGroupList {
    lateinit var welcomeGroupList:ArrayList<Long>
    lateinit var managerGroupList:ArrayList<Long>
    lateinit var fuckPsSister:ArrayList<Long>
    lateinit var fakeInfoIdentityHashMap: HashMap<Long, Boolean>
    lateinit var luckyGroup: HashMap<Long, Boolean>
    lateinit var lineArtGroup:HashMap<Long,Boolean>
    fun init(){
        luckyGroup = HashMap()
        lineArtGroup = HashMap()
        fakeInfoIdentityHashMap = HashMap()
        welcomeGroupList = ArrayList()
        managerGroupList = ArrayList()
        fuckPsSister = ArrayList()
    }
}




object BotsManager : CoroutineScope,EventListener {
    var oneBot: Bot? = null
    val task = TimerSessionManager()  // 命令调度器
    suspend fun loginBot():Bot {
        oneBot =  BotFactory.newBot(
                qq = AppConfig.getInstance().botQQ.toLong(),
                password = AppConfig.getInstance().botPwd
        ) {
            this.protocol = when(AppConfig.getInstance().device){
                "PHONE"->BotConfiguration.MiraiProtocol.ANDROID_PAD;
                "WATCH"->BotConfiguration.MiraiProtocol.ANDROID_WATCH
                else -> BotConfiguration.MiraiProtocol.ANDROID_PAD
            }
            val deviceInfoFolder = File("devices")
            if (!deviceInfoFolder.exists()) {
                deviceInfoFolder.mkdir()
            }
            fileBasedDeviceInfo(File(deviceInfoFolder,
                    "${AppConfig.getInstance().botQQ.toLong()}.json").absolutePath)
        }
        oneBot?.login()
        return oneBot as Bot
    }
    val jobs = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+ SupervisorJob(appJob) + jobs
    fun closeAllBot() {
        jobs.cancel()
    }
}
