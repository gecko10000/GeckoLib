package gecko10000.geckolib.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * A utility class to easily modify items
 */
public class ItemUtils {

    /**
     * Counts the number of the given item in the given inventory
     *
     * @param inv       The inventory to count the items in
     * @param predicate A predicate to compare items for counting
     * @return The number of items found
     */
    public static int count(Inventory inv, Predicate<ItemStack> predicate) {
        int count = 0;
        for (ItemStack i : inv) {
            if (predicate.test(i)) {
                count += i.getAmount();
            }
        }
        return count;
    }

    /**
     * Counts the number of the given item in the given inventory
     *
     * @param inv  The inventory to count the items in
     * @param item The item to count
     * @return The number of items found
     */
    public static int count(Inventory inv, ItemStack item) {
        return count(inv, item::isSimilar);
    }

    /**
     * Removes the specified amount of the given item from the given inventory
     *
     * @param inv       The inventory to remove the items from
     * @param amount    The amount of items to remove
     * @param predicate A predicate to compare items for removal
     * @return Whether the amount specified could be removed. False if it removed less than specified.
     */
    public static boolean remove(Inventory inv, int amount, Predicate<ItemStack> predicate) {
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length && amount > 0; i++) {
            if (!predicate.test(contents[i])) {
                continue;
            }
            if (amount >= contents[i].getAmount()) {
                amount -= contents[i].getAmount();
                contents[i] = null;
                if (amount == 0) {
                    inv.setContents(contents);
                    return true;
                }
                continue;
            }
            contents[i].setAmount(contents[i].getAmount() - amount);
            inv.setContents(contents);
            return true;
        }
        inv.setContents(contents);
        return false;
    }

    /**
     * Removes the specified amount of the given item from the given inventory
     *
     * @param inv    The inventory to remove the items from
     * @param item   The item to be removed
     * @param amount The amount of items to remove
     * @return Whether the amount specified could be removed. False if it removed less than specified.
     */
    public static boolean remove(Inventory inv, ItemStack item, int amount) {
        return remove(inv, amount, item::isSimilar);
    }

    /**
     * Removes the specified amount of the given item type from the given inventory
     *
     * @param inv    The inventory to remove the items from
     * @param type   The item type to be removed
     * @param amount The amount of items to remove
     * @return Whether the amount specified could be removed. False if it removed less than specified.
     */
    public static boolean remove(Inventory inv, Material type, int amount) {
        return remove(inv, amount, (i) -> i.getType() == type);
    }

    /**
     * Remove all matching items up to a maximum, returning the number that were removed
     *
     * @param inv        The inventory to count and remove items from
     * @param max        The maximum number of items to remove
     * @param comparison A predicate to compare items for counting and removal
     * @return How many items were removed
     */
    public static int countAndRemove(Inventory inv, int max, Predicate<ItemStack> comparison) {
        int count = count(inv, comparison);
        count = Math.min(max, count);
        remove(inv, count, comparison);
        return count;
    }

    /**
     * Remove all matching items up to a maximum, returning the number that were removed
     *
     * @param inv  The inventory to count and remove items from
     * @param item The item to count and remove
     * @param max  The maximum number of items to remove
     * @return How many items were removed
     */
    public static int countAndRemove(Inventory inv, ItemStack item, int max) {
        return countAndRemove(inv, max, item::isSimilar);
    }

    /**
     * Remove all matching items up to a maximum, returning the number that were removed
     *
     * @param inv  The inventory to count and remove items from
     * @param type The item type to count and remove
     * @param max  The maximum number of items to remove
     * @return How many items were removed
     */
    public static int countAndRemove(Inventory inv, Material type, int max) {
        return countAndRemove(inv, max, (i) -> i.getType() == type);
    }

    /**
     * Remove all matching items, returning the number that were removed
     *
     * @param inv  The inventory to count and remove items from
     * @param item The item to count and remove
     * @return How many items were removed
     */
    public static int countAndRemove(Inventory inv, ItemStack item) {
        return countAndRemove(inv, Integer.MAX_VALUE, item::isSimilar);
    }

    /**
     * Remove all items of a specified type, returning the number that were removed
     *
     * @param inv  The inventory to count and remove items from
     * @param type The item type to count and remove
     * @return How many items were removed
     */
    public static int countAndRemove(Inventory inv, Material type) {
        return countAndRemove(inv, Integer.MAX_VALUE, (i) -> i.getType() == type);
    }

    /**
     * Give the player the specified items, dropping them on the ground if there is not enough room
     *
     * @param player The player to give the items to
     * @param items  The items to be given
     */
    public static void give(Player player, ItemStack... items) {
        player.getInventory().addItem(items).values().forEach(i -> player.getWorld().dropItem(player.getLocation(), i));
    }

    /**
     * Gives the player the specified amount of the specified item, dropping them on the ground if there is not enough room
     *
     * @param player The player to give the items to
     * @param item   The item to be given to the player
     * @param amount The amount the player should be given
     */
    public static void give(Player player, ItemStack item, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        int stackSize = item.getType().getMaxStackSize();
        while (amount > stackSize) {
            ItemStack clone = item.clone();
            clone.setAmount(stackSize);
            give(player, clone);
            amount -= stackSize;
        }
        ItemStack clone = item.clone();
        clone.setAmount(amount);
        give(player, clone);
    }

    /**
     * Gives the player the specified amount of the specified item type, dropping them on the ground if there is not enough room
     *
     * @param player The player to give the items to
     * @param type   The item type to be given to the player
     * @param amount The amount the player should be given
     */
    public static void give(Player player, Material type, int amount) {
        give(player, new ItemStack(type), amount);
    }

    /**
     * Calculates the minimum chest size (next highest multiple of 9) required to fit the given number of item stacks
     *
     * @param items The number of item stacks
     * @return The minimum chest size to accommodate the items
     */
    public static int minimumChestSize(int items) {
        return (int) Math.max(9, Math.ceil(items / 9d) * 9);
    }

    /**
     * Checks whether an item is empty, meaning it is either null or air
     *
     * @param item The item to check
     * @return Whether the item is empty
     */
    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR || item.getAmount() <= 0;
    }

}
