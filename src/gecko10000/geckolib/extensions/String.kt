package gecko10000.geckolib.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor


fun String.asClickable(insertText: String): Component {
    return parseMM("$this (click to paste old value):")
        .withDefaults(color = NamedTextColor.GREEN)
        .hoverEvent(parseMM("<green>Click to paste!").asHoverEvent())
        .clickEvent(ClickEvent.suggestCommand(insertText))
}
