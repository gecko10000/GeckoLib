package gecko10000.geckolib

import org.bukkit.command.CommandSender
import redempt.redlib.commandmanager.ArgType
import java.util.stream.Stream

enum class StateVerb {
    ON,
    OFF,
    TOGGLE,
    ;

    fun getNewBoolean(currentValue: Boolean): Boolean {
        return when (this) {
            ON -> true
            OFF -> false
            TOGGLE -> !currentValue
        }
    }

    companion object {
        private val commandConverter: (String) -> StateVerb = { s -> StateVerb.valueOf(s.uppercase()) }
        private val tabStream: (CommandSender) -> Stream<String> =
            { StateVerb.entries.map { it.name.lowercase() }.stream() }
        val argType = ArgType("stateVerb", commandConverter).tabStream(tabStream)
    }

}
