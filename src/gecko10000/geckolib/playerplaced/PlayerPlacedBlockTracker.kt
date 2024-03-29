package gecko10000.geckolib.playerplaced

import gecko10000.geckolib.GeckoLib
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.event.EventPriority
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.persistence.PersistentDataType
import redempt.redlib.misc.EventListener


const val LOWER_4 = (1 shl 4) - 1

class PlayerPlacedBlockTracker internal constructor() {

    companion object {
        val instance by lazy { PlayerPlacedBlockTracker() }
    }

    private val blockDataKey by lazy { NamespacedKey(GeckoLib.get(), "player_placed") }

    private fun Block.toBlockPos(): BlockPos {
        return BlockPos(
            x = this.x and LOWER_4,
            y = this.y,
            z = this.z and LOWER_4
        )
    }

    fun isPlayerPlaced(block: Block): Boolean {
        val chunk = block.chunk
        val string = chunk.persistentDataContainer.get(blockDataKey, PersistentDataType.STRING) ?: return false
        //println("Checking: ${ChunkBlockPositions.deserialize(string)}")
        return ChunkBlockPositions.deserialize(string).blocks.contains(block.toBlockPos())
    }

    fun addBlocks(blocks: Iterable<Block>) {
        val chunk = blocks.firstOrNull()?.chunk ?: return
        val string = chunk.persistentDataContainer.get(blockDataKey, PersistentDataType.STRING)
        val newPositions = if (string == null) {
            ChunkBlockPositions(blocks.map { it.toBlockPos() }.toSet())
        } else {
            val positions = ChunkBlockPositions.deserialize(string)
            positions.copy(blocks = positions.blocks.plus(blocks.map { it.toBlockPos() }.toSet()))
        }
        //println("Added: $newPositions")
        chunk.persistentDataContainer.set(blockDataKey, PersistentDataType.STRING, newPositions.serialize())
    }

    fun addBlock(block: Block) = addBlocks(setOf(block))

    fun removeBlocks(blocks: Iterable<Block>) {
        val chunk = blocks.firstOrNull()?.chunk ?: return
        val string = chunk.persistentDataContainer.get(blockDataKey, PersistentDataType.STRING) ?: return
        val positions = ChunkBlockPositions.deserialize(string)
        val newPositions = positions.copy(blocks = positions.blocks.minus(blocks.map { it.toBlockPos() }.toSet()))
        //println("Removed: $newPositions")
        chunk.persistentDataContainer.set(blockDataKey, PersistentDataType.STRING, newPositions.serialize())
    }

    fun removeBlock(block: Block) = removeBlocks(setOf(block))

    init {
        EventListener(BlockPlaceEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            addBlock(e.blockPlaced)
        }
        EventListener(BlockBreakEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            removeBlock(e.block)
        }
        EventListener(BlockExplodeEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            removeBlocks(e.blockList())
        }
        EventListener(EntityExplodeEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            removeBlocks(e.blockList())
        }
        EventListener(BlockBurnEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            removeBlock(e.block)
        }
        EventListener(BlockPistonExtendEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            val sources = e.blocks
            val destinations = sources.map { it.getRelative(e.direction) }
            removeBlocks(sources)
            addBlocks(destinations)
        }
        EventListener(EntityChangeBlockEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            removeBlock(e.block)
        }
    }

}
