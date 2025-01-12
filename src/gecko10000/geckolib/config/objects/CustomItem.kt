@file:UseSerializers(MMComponentSerializer::class)

package gecko10000.geckolib.config.objects

import com.charleskorn.kaml.YamlComment
import gecko10000.geckolib.config.serializers.MMComponentSerializer
import gecko10000.geckolib.extensions.withDefaults
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


@Serializable
data class CustomItem(
    val name: Component,
    val lore: List<Component> = listOf(),
    val material: Material,
    val glowing: Boolean = false,
    @YamlComment("This uniquely identifies the item. Changing it will break existing items.")
    val tag: String,
) {
    @Transient
    private val key = NamespacedKey("geckolib", tag)

    @Transient
    private val _item = ItemStack(material).apply {
        editMeta {
            it.displayName(name.withDefaults())
            it.lore(this@CustomItem.lore.map(Component::withDefaults))
            it.persistentDataContainer.set(key, PersistentDataType.BOOLEAN, true)
            if (glowing) {
                it.setEnchantmentGlintOverride(true)
            }
        }
    }
    val item
        get() = _item.clone()

    fun isCustomItem(itemStack: ItemStack): Boolean {
        return !itemStack.isEmpty && itemStack.itemMeta.persistentDataContainer.has(key)
    }


}
