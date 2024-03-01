package gecko10000.geckolib.extensions

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

fun ItemMeta.addGlow() {
    if (hasEnchants()) return
    addEnchant(Enchantment.DURABILITY, 1, true)
    addItemFlags(ItemFlag.HIDE_ENCHANTS)
}
