@file:UseSerializers(MMComponentSerializer::class)

package gecko10000.geckolib.config.objects

import gecko10000.geckolib.config.serializers.MMComponentSerializer
import gecko10000.geckolib.extensions.withDefaults
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


@Serializable
data class DisplayItem(
    val name: Component,
    val lore: List<Component> = listOf(),
    val material: Material,
    val glowing: Boolean = false,
    val amount: Int = 1
) {
    @Transient
    private val _item = ItemStack(material).apply {
        amount = this@DisplayItem.amount
        editMeta {
            it.displayName(name.withDefaults())
            it.lore(this@DisplayItem.lore.map(Component::withDefaults))
            if (glowing) {
                it.setEnchantmentGlintOverride(true)
            }
        }
    }
    val item
        get() = _item.clone()
}
