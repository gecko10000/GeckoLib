package gecko10000.geckolib.blockdata

import org.bukkit.Chunk
import org.bukkit.block.Block

data class ChunkPos(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    init {
        if (x < 0 || x >= 16 || z < 0 || z >= 16) {
            throw Exception("Invalid block pos $x/$y/$z (x and z must be in 0..15)")
        }
    }

    fun toId(): String {
        val xHex = x.toString(16)
        val zHex = z.toString(16)
        val yHex = y.toString(36)
        return "$xHex$zHex$yHex"
    }

    fun ofChunk(chunk: Chunk): Block {
        return chunk.getBlock(x, y, z)
    }

    companion object {
        fun fromId(key: String): ChunkPos {
            val x = key[0].digitToInt(16)
            val z = key[1].digitToInt(16)
            val y = key.substring(2).toInt(36)
            return ChunkPos(x, y, z)
        }

        private const val LOWER_4 = (1 shl 4) - 1
        fun fromBlock(block: Block): ChunkPos {
            return ChunkPos(
                x = block.x and LOWER_4,
                y = block.y,
                z = block.z and LOWER_4
            )
        }
    }
}
