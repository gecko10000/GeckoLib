package gecko10000.geckolib.playerplaced

data class BlockPos(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun toKey(): String {
        val xHex = x.toString(16)
        val zHex = z.toString(16)
        return "$xHex$zHex$y"
    }

    companion object {
        fun fromKey(key: String): BlockPos {
            val x = key[0].digitToInt(16)
            val z = key[1].digitToInt(16)
            val y = key.substring(2).toInt()
            return BlockPos(x, y, z)
        }
    }
}
