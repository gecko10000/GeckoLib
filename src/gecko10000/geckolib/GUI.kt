package gecko10000.geckolib

import gecko10000.geckolib.extensions.parseMM
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.TooltipDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import redempt.redlib.inventorygui.InventoryGUI
import redempt.redlib.inventorygui.ItemButton
import redempt.redlib.misc.Task

@Suppress("UnstableApiUsage")
abstract class GUI(val player: Player) : InventoryHolder {

    abstract fun createInventory(): InventoryGUI
    protected val inventory: InventoryGUI by lazy { createInventory() }
    final override fun getInventory(): Inventory = inventory.inventory
    var shouldOpen: Boolean = true

    init {
        Task.syncDelayed { ->
            if (shouldOpen)
                inventory.open(player)
            else
                player.closeInventory()
        }
    }

    companion object {
        val FILLER: ItemStack = run {
            val item = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
            item.editMeta {
                it.displayName(Component.empty())
            }
            item.setData(
                DataComponentTypes.TOOLTIP_DISPLAY,
                TooltipDisplay.tooltipDisplay().hideTooltip(true).build()
            )
            item
        }

        fun BACK(callback: () -> Unit): ItemButton {
            val item = ItemStack.of(Material.RED_STAINED_GLASS_PANE)
            item.editMeta {
                it.displayName(parseMM("<red>Back"))
            }
            return ItemButton.create(item) { e -> callback() }
        }
    }

}
