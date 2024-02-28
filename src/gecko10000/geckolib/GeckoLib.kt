package gecko10000.geckolib

import gecko10000.geckolib.extensions.MM
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class GeckoLib : JavaPlugin() {
    override fun onDisable() {
        Bukkit.getOnlinePlayers()
        .filter { it.openInventory.topInventory.holder is GUI }
        .forEach { player ->
            player.closeInventory()
            player.sendMessage(MM.deserialize("<red>Plugin reloading."))
        }
    }
}
