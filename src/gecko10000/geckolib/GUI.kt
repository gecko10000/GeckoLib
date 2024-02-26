package gecko10000.geckolib

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import redempt.redlib.inventorygui.InventoryGUI
import redempt.redlib.misc.Task

abstract class GUI(val player: Player) : InventoryHolder {

    abstract fun createInventory(): InventoryGUI
    private val inventory: InventoryGUI by lazy { createInventory() }
    final override fun getInventory(): Inventory = inventory.inventory

    init {
        Task.syncDelayed { -> inventory.open(player) }
    }

}
