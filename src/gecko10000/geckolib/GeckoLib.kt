package gecko10000.geckolib

import gecko10000.geckolib.inventorygui.GUI
import gecko10000.geckolib.playerplaced.PlayerPlacedBlockTracker
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class GeckoLib : JavaPlugin() {

    override fun onEnable() {
        instance = this
        PlayerPlacedBlockTracker
    }

    override fun onDisable() {
        Bukkit.getOnlinePlayers()
            .filter { it.openInventory.topInventory.holder is GUI }
            .forEach { player ->
                player.closeInventory()
            }
    }

    companion object {
        private lateinit var instance: GeckoLib
        internal fun get() = instance
    }
}
