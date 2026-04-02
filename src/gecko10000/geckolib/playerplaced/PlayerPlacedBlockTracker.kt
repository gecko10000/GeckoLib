package gecko10000.geckolib.playerplaced

import com.destroystokyo.paper.event.block.BlockDestroyEvent
import gecko10000.geckolib.blockdata.BlockDataManager
import gecko10000.geckolib.misc.EventListener
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.world.StructureGrowEvent
import org.bukkit.persistence.PersistentDataType


object PlayerPlacedBlockTracker {

    private val gravityBlockTrackKey = NamespacedKey("gl", "pp")

    private val bdm = BlockDataManager("pp", PersistentDataType.BOOLEAN)

    fun isPlayerPlaced(block: Block) = bdm.contains(block)

    fun addBlocks(blocks: Iterable<Block>) = blocks.forEach(this::addBlock)

    fun addBlock(block: Block) {
        bdm[block] = true
    }

    fun removeBlocks(blocks: Iterable<Block>) = blocks.forEach(this::removeBlock)

    fun removeBlock(block: Block) = bdm.remove(block)

    init {
        EventListener(BlockPlaceEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            addBlock(e.block)
        }
        // Endermen, silverfish
        EventListener(EntityChangeBlockEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            if (e.entityType == EntityType.FALLING_BLOCK) return@EventListener
            bdm.remove(e.block)
        }
        EventListener(
            StructureGrowEvent::class.java,
            EventPriority.MONITOR
        ) { e ->
            if (e.isCancelled) return@EventListener
            e.blocks.map { it.block }.forEach(bdm::remove)
        }
        EventListener(BlockDestroyEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            bdm.remove(e.block)
        }
        // Track falling blocks.
        EventListener(EntityChangeBlockEvent::class.java, EventPriority.MONITOR) { e ->
            if (e.isCancelled) return@EventListener
            if (e.entityType != EntityType.FALLING_BLOCK) return@EventListener
            val isLanding = e.to != Material.AIR
            if (isLanding && e.entity.persistentDataContainer.has(gravityBlockTrackKey)) {
                bdm[e.block] = true
            }
            if (!isLanding && bdm.contains(e.block)) {
                e.entity.persistentDataContainer.set(gravityBlockTrackKey, PersistentDataType.BOOLEAN, true)
            }
        }
    }

}
