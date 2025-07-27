package gecko10000.geckolib.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.potion.PotionEffectType

class PotionEffectTypeSerializer : KSerializer<PotionEffectType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PotionEffectType", PrimitiveKind.STRING)
    private val registry = Registry.MOB_EFFECT

    override fun deserialize(decoder: Decoder): PotionEffectType {
        // https://github.com/charleskorn/kaml/issues/300
        return registry.getOrThrow(NamespacedKey.fromString(decoder.decodeString())!!)
    }

    override fun serialize(encoder: Encoder, value: PotionEffectType) {
        val string = value.key.asString()
        encoder.encodeString(string)
    }
}
