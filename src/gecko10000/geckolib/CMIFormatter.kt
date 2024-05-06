package gecko10000.geckolib

import gecko10000.geckolib.extensions.withDefaults
import net.Zrips.CMILib.Colors.CMIChatColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object CMIFormatter {

    private val componentSerializer = LegacyComponentSerializer.legacySection()

    fun color(string: String, withDefaults: Boolean = true): Component {
        val parsed = CMIChatColor.colorize(string)
        return componentSerializer.deserialize(parsed).let {
            if (withDefaults) it.withDefaults() else it
        }
    }

    fun uncolor(component: Component): String {
        val deserialized = componentSerializer.serialize(component)
        return CMIChatColor.deColorize(deserialized)
    }

}
