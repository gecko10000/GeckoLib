package gecko10000.geckolib.blockdata

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventPriority
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityExplodeEvent
import redempt.redlib.misc.EventListener

class BlockDataListener<P : Any>(private val manager: BlockDataManager<*, P>) {

    private fun handlePiston(blocks: List<Block>, direction: BlockFace) {
        val destinations = blocks.associateWith { it.getRelative(direction) }
        val blockValues = blocks.associateWith { manager[it] }.filterValues { it != null }
        for (blockValue in blockValues.entries) {
            val newBlock = destinations.getValue(blockValue.key)
            // Always set data in destination
            manager[newBlock] = blockValue.value!!
            // Only clear source if it's not a destination
            if (blockValue.key !in destinations.values) {
                manager.remove(blockValue.key)
            }
        }
    }

    init {
        EventListener(BlockBreakEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            manager.remove(e.block)
        }
        EventListener(BlockExplodeEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            e.blockList().forEach { manager.remove(it) }
        }
        EventListener(EntityExplodeEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            e.blockList().forEach { manager.remove(it) }
        }
        EventListener(BlockBurnEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            manager.remove(e.block)
        }
        EventListener(BlockPistonExtendEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            handlePiston(e.blocks, e.direction)
        }
        EventListener(BlockPistonRetractEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            handlePiston(e.blocks, e.direction)
        }
        EventListener(EntityChangeBlockEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            manager.remove(e.block)
        }
    }
}
