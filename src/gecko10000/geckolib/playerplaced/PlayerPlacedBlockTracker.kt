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


class PlayerPlacedBlockTracker internal constructor() {

    companion object {
        val instance by lazy { PlayerPlacedBlockTracker() }
        const val KEY_PREFIX: String = "pp_"
        const val LOWER_4 = (1 shl 4) - 1
    }

    private fun Block.toBlockPos(): BlockPos {
        return BlockPos(
            x = this.x and LOWER_4,
            y = this.y,
            z = this.z and LOWER_4
        )
    }

    private fun BlockPos.toNamespacedKey(): NamespacedKey {
        return NamespacedKey(GeckoLib.get(), "$KEY_PREFIX${this.toKey()}")
    }

    fun isPlayerPlaced(block: Block): Boolean {
        val chunk = block.chunk
        val blockPos = block.toBlockPos()
        return chunk.persistentDataContainer.has(blockPos.toNamespacedKey())
    }

    fun addBlocks(blocks: Iterable<Block>) = blocks.forEach(this::addBlock)

    fun addBlock(block: Block) {
        val blockPos = block.toBlockPos()
        block.chunk.persistentDataContainer.set(blockPos.toNamespacedKey(), PersistentDataType.BOOLEAN, true)
    }

    fun removeBlocks(blocks: Iterable<Block>) = blocks.forEach(this::removeBlock)

    fun removeBlock(block: Block) {
        val blockPos = block.toBlockPos()
        block.chunk.persistentDataContainer.remove(blockPos.toNamespacedKey())
    }

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
