package gecko10000.geckolib.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class MMComponentSerializer : KSerializer<Component> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MMComponent", PrimitiveKind.STRING)

    private val miniMessage = MiniMessage.miniMessage()

    override fun deserialize(decoder: Decoder): Component {
        return miniMessage.deserialize(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeString(miniMessage.serialize(value))
    }
}
