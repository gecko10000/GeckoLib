package gecko10000.geckolib.playerplaced


// x,z : ys <1/5>: [1,2]
data class ChunkBlockPositions(
    val blocks: Set<BlockPos>,
) {
    companion object {
        fun deserialize(serialized: String): ChunkBlockPositions {
            val blocks = mutableSetOf<BlockPos>()
            var currentXZPos: Pair<Int, Int>? = null
            var i = 0
            while (i < serialized.length) {
                val char = serialized[i]
                if (currentXZPos == null) {
                    currentXZPos = decodePos(char)
                    i++
                    continue
                }
                val yStrings = serialized.substring(i).substringBefore(']')
                val yPositions = yStrings.split(",").mapNotNull { it.toIntOrNull() }
                val blockPositions = yPositions.map {
                    BlockPos(currentXZPos!!.first, it, currentXZPos!!.second)
                }
                blocks.addAll(blockPositions)
                currentXZPos = null
            }
            return ChunkBlockPositions(blocks)
        }

        private fun encodePos(x: Int, z: Int): Char = (x + (z shl 4)).toChar()
        private fun decodePos(char: Char): Pair<Int, Int> {
            val combined = char.code
            return (combined and LOWER_4) to (combined shr 4)
        }
    }

    fun serialize(): String {
        val xzMap = mutableMapOf<Char, Set<Int>>()
        for (pos in blocks) {
            val key = encodePos(pos.x, pos.z)
            xzMap.compute(key) { _, set ->
                set.orEmpty().plus(pos.y)
            }
        }
        return xzMap.mapValues { e ->
            "${e.value.joinToString(separator = ",") { it.toString() }}]"
        }.entries.joinToString(separator = "") { "${it.key}${it.value}" }
    }
}

data class BlockPos(
    val x: Int,
    val y: Int,
    val z: Int,
)
