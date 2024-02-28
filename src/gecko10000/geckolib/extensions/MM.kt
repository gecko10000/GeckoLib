package gecko10000.geckolib.extensions

import net.kyori.adventure.text.minimessage.MiniMessage


val MM = MiniMessage.miniMessage()

fun parseMM(value: String, withDefaults: Boolean = true) = MM.deserialize(value).let {
    if (withDefaults) it.withDefaults() else it
}
