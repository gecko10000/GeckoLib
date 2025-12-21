package gecko10000.geckolib.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class LocationSerializer : KSerializer<Location> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("location", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Location {
        val string = decoder.decodeString()
        val split = string.split("/")
        val world = Bukkit.getWorld(UUID.fromString(split[0]))
        val x = split[1].toDouble()
        val y = split[2].toDouble()
        val z = split[3].toDouble()
        val yaw = split[4].toFloat()
        val pitch = split[5].toFloat()
        return Location(world, x, y, z, yaw, pitch)
    }

    override fun serialize(encoder: Encoder, value: Location) {
        val builder = StringJoiner("/")
        builder.add(value.world.uid.toString())
        builder.add(value.x.toString())
        builder.add(value.y.toString())
        builder.add(value.z.toString())
        builder.add(value.yaw.toString())
        builder.add(value.pitch.toString())
        encoder.encodeString(builder.toString())
    }
}
