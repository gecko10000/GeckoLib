package gecko10000.geckolib.misc;

import gecko10000.geckolib.GeckoLib;
import gecko10000.geckolib.extensions.MMKt;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChatPrompt implements Listener {

    private static final String CANCEL_INPUT = "cancel";
    private static final Component CANCEL_MESSAGE = MMKt.parseMM(
            "<red>Type \"" + CANCEL_INPUT + "\" to cancel.", false
    );

    private static final Map<Player, Prompt> prompts = new HashMap<>();

    static {
        new ChatPrompt();
    }

    /**
     * Prompts a player with callbacks for player response and cancelling
     *
     * @param player            The player to prompt
     * @param prompt            The prompt to send to the player, or null for no prompt
     * @param showCancelMessage Whether to show the cancel message to the player
     * @param onResponse        The callback for when the player responds
     * @param onCancel          The callback for when the prompt is cancelled
     */
    public static void prompt(Player player, @Nullable Component prompt, boolean showCancelMessage,
                              Consumer<String> onResponse,
                              Consumer<CancelReason> onCancel) {
        Prompt removed = prompts.remove(player);
        if (removed != null) {
            removed.cancel(CancelReason.PROMPT_OVERRIDDEN);
        }
        if (prompt != null) {
            player.sendMessage(prompt);
        }
        prompts.put(player, new Prompt(onResponse, onCancel));
        if (showCancelMessage) {
            player.sendMessage(CANCEL_MESSAGE);
        }
    }

    /**
     * Prompts a player with callbacks for player response and cancelling
     *
     * @param player            The player to prompt
     * @param prompt            The prompt to send to the player, or null for no prompt
     * @param showCancelMessage Whether to show the cancel message to the player
     * @param onResponse        The callback for when the player responds
     */
    public static void prompt(Player player, Component prompt, boolean showCancelMessage, Consumer<String> onResponse) {
        prompt(player, prompt, showCancelMessage, onResponse, c -> {
        });
    }

    /**
     * Prompts a player with callbacks for player response and cancelling
     *
     * @param player     The player to prompt
     * @param prompt     The prompt to send to the player, or null for no prompt
     * @param onResponse The callback for when the player responds
     */
    public static void prompt(Player player, Component prompt, Consumer<String> onResponse) {
        prompt(player, prompt, true, onResponse, c -> {
        });
    }

    /**
     * Prompts a player with callbacks for player response and cancelling
     *
     * @param player     The player to prompt
     * @param prompt     The prompt to send to the player, or null for no prompt
     * @param onResponse The callback for when the player responds
     * @param onCancel   The callback for when the prompt is cancelled
     */
    public static void prompt(Player player, Component prompt, Consumer<String> onResponse,
                              Consumer<CancelReason> onCancel) {
        prompt(player, prompt, true, onResponse, onCancel);
    }

    private ChatPrompt() {
        Bukkit.getPluginManager().registerEvents(this, GeckoLib.Companion.get$GeckoLib());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent e) {
        Prompt p = prompts.remove(e.getPlayer());
        if (p == null) {
            return;
        }
        e.setCancelled(true);
        String message = PlainTextComponentSerializer.plainText().serialize(e.originalMessage());
        if (message.equalsIgnoreCase(CANCEL_INPUT)) {
            p.cancel(CancelReason.PLAYER_CANCELLED);
            return;
        }
        p.respond(message);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Prompt p = prompts.remove(e.getPlayer());
        if (p != null) {
            p.cancel(CancelReason.PLAYER_LEFT);
        }
    }

    private record Prompt(Consumer<String> onResponse, Consumer<CancelReason> onCancel) {
        public void respond(String response) {
            onResponse.accept(response);
        }

        public void cancel(CancelReason reason) {
            onCancel.accept(reason);
        }
    }

    public enum CancelReason {
        /**
         * Passed when the player was given another prompt. This prompt is removed and cancelled.
         */
        PROMPT_OVERRIDDEN,
        /**
         * Passed when the prompt was cancelled because the player typed 'cancel'.
         */
        PLAYER_CANCELLED,
        /**
         * Passed when the prompt was cancelled because the player left the server.
         */
        PLAYER_LEFT
    }

}
