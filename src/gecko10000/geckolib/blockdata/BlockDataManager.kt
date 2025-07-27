package gecko10000.geckolib.blockdata

import org.bukkit.Chunk
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataType

// It's preferable to use a short prefix because
// it will directly impact storage usage.
// P and C extend Any because they're not nullable.
class BlockDataManager<P : Any, C : Any>(
    private val prefix: String,
    private val type: PersistentDataType<P, C>,
    private val events: Boolean = true,
) {

    private companion object {
        const val NAMESPACE = "gl"
    }

    init {
        if (events) {
            BlockDataListener(this)
        }
    }

    private fun createKey(block: Block): NamespacedKey {
        val pos = ChunkPos.fromBlock(block)
        return NamespacedKey(NAMESPACE, "$prefix${pos.toId()}")
    }

    operator fun set(block: Block, data: C) = block.chunk.persistentDataContainer.set(createKey(block), type, data)
    operator fun get(block: Block): C? = block.chunk.persistentDataContainer.get(createKey(block), type)
    fun getValue(block: Block) = get(block)!!
    fun remove(block: Block): C? {
        val key = createKey(block)
        val pdc = block.chunk.persistentDataContainer
        val removedValue = pdc.get(key, type)
        pdc.remove(key)
        return removedValue
    }

    fun contains(block: Block) = block.chunk.persistentDataContainer.has(createKey(block), type)
    fun getValuedBlocks(chunk: Chunk): Set<Block> {
        val allKeys = chunk.persistentDataContainer.keys
        val ourKeys = allKeys.filter { it.namespace == NAMESPACE && it.value().startsWith(prefix) }
        val posIds = ourKeys.map { it.value().substringAfter(prefix) }
        return posIds.map { ChunkPos.fromId(it).ofChunk(chunk) }.toSet()
    }

}
