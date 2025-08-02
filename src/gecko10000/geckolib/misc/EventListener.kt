package gecko10000.geckolib.misc

import gecko10000.geckolib.GeckoLib
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class EventListener<T : Event>(
    private val clazz: Class<T>,
    priority: EventPriority,
    private val listener: (T) -> Unit
) : Listener {

    init {
        Bukkit.getServer().pluginManager.registerEvent(
            clazz, this, priority,
            { l, e -> handleEvent(e as T) },
            GeckoLib.Companion.get()
        )
    }

    private fun handleEvent(event: T) {
        if (clazz.isAssignableFrom(event.javaClass)) {
            listener(event);
        }
    }

    constructor(clazz: Class<T>, listener: (T) -> Unit) : this(clazz, EventPriority.NORMAL, listener)

    fun unregister() {
        HandlerList.unregisterAll(this)
    }

}
