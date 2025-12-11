package gecko10000.geckolib.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import org.bukkit.Registry
import org.bukkit.Sound

class SoundSerializer : KSerializer<Sound> {

    private val soundRegistry = Registry.SOUNDS

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("sound", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Sound) {
        encoder.encodeString(soundRegistry.getKeyOrThrow(value).asString())
    }

    override fun deserialize(decoder: Decoder): Sound {
        val key = Key.key(decoder.decodeString())
        return soundRegistry[key]!!
    }
}
