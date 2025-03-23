package gecko10000.geckolib.playerplaced

import gecko10000.geckolib.blockdata.BlockDataManager
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataType


object PlayerPlacedBlockTracker {

    private val bdm = BlockDataManager("pp", PersistentDataType.BOOLEAN)

    fun isPlayerPlaced(block: Block) = bdm.contains(block)

    fun addBlocks(blocks: Iterable<Block>) = blocks.forEach(this::addBlock)

    fun addBlock(block: Block) {
        bdm[block] = true
    }

    fun removeBlocks(blocks: Iterable<Block>) = blocks.forEach(this::removeBlock)

    fun removeBlock(block: Block) = bdm.remove(block)

}
