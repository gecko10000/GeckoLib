package gecko10000.geckolib.extensions

import org.bukkit.block.BlockFace
import org.bukkit.util.Vector
import kotlin.math.abs

fun Vector.toBlockFace(): BlockFace {
    val absX = abs(this.x)
    val absY = abs(this.y)
    val absZ = abs(this.z)
    if (absY > absX && absY > absZ) {
        return if (this.y >= 0) BlockFace.UP else BlockFace.DOWN
    }
    if (absX > absZ) {
        return if (this.x >= 0) BlockFace.EAST else BlockFace.WEST
    }
    return if (this.z >= 0) BlockFace.SOUTH else BlockFace.NORTH
}
