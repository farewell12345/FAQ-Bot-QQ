package ink.umb.faqbot.dto.model

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object Welcome: Table<Nothing>("welcome") {
    val id =int("id").primaryKey()
    val group = long("group")
    val talk = varchar("talk")
}
