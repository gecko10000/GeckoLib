package gecko10000.geckolib.extensions

import org.bukkit.inventory.meta.ItemMeta

fun ItemMeta.addGlow() {
    if (hasEnchants()) return
    this.setEnchantmentGlintOverride(true)
}
