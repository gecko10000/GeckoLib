package gecko10000.geckolib.config.serializers

import com.google.gson.JsonParser
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

class ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ItemStack", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ItemStack {
        val string = decoder.decodeString()
        val json = JsonParser.parseString(string).asJsonObject
        return Bukkit.getUnsafe().deserializeItemFromJson(json)
    }

    override fun serialize(encoder: Encoder, value: ItemStack) {
        val json = Bukkit.getUnsafe().serializeItemAsJson(value)
        encoder.encodeString(json.toString())
    }
}
