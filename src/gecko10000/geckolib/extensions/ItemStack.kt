package gecko10000.geckolib.extensions

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

fun ItemStack?.isEmpty(): Boolean {
    return this == null || this.isEmpty
}

fun ItemStack.name(): Component {
    val meta = this.itemMeta
    val backupName = if (meta.hasItemName())
        meta.itemName()
    else
        Component.translatable(this.translationKey())
    return meta.customName() ?: backupName
}
