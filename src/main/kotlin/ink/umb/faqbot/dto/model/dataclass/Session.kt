package ink.umb.faqbot.dto.model.dataclass

import net.mamoe.mirai.contact.Group

data class Session(
    val group: Group,
    val user:Long,
    val data:String = "",
    val type:String
)
