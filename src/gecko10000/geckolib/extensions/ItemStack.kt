package gecko10000.geckolib.extensions

import org.bukkit.inventory.ItemStack

fun ItemStack?.isEmpty(): Boolean {
    return this == null || this.isEmpty
}
