package gecko10000.geckolib

import gecko10000.geckolib.extensions.MM
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import redempt.redlib.misc.Task
import java.util.Timer
import java.util.TimerTask

class GeckoLib : JavaPlugin() {
    override fun onDisable() {
        Bukkit.getOnlinePlayers()
        .filter { it.openInventory.topInventory.holder is GUI }
        .forEach { player ->
            player.closeInventory()
            // TODO: maybe just remove this?
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    player.sendMessage(MM.deserialize("<red>Plugin reloading."))
                }

            }, 1000L)
        }
    }
}
