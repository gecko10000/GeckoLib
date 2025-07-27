package gecko10000.geckolib.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.inventory.ItemStack

class InternalItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("InternalItemStack", PrimitiveKind.STRING)

    @OptIn(ExperimentalStdlibApi::class)
    override fun deserialize(decoder: Decoder): ItemStack {
        return ItemStack.deserializeBytes(decoder.decodeString().hexToByteArray())
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun serialize(encoder: Encoder, value: ItemStack) {
        val string = value.serializeAsBytes().toHexString()
        encoder.encodeString(string)
    }
}
