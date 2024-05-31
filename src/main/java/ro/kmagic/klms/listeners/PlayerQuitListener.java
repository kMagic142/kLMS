package ro.kmagic.klms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.utils.MessageUtils;

public class PlayerQuitListener implements Listener, MessageUtils {
    KLMS instance;

    public PlayerQuitListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (this.instance.getPlayers().contains(player)) {
            this.instance.getGameManager().removePlayer(player);

            GameStatus gameStatus = this.instance.getGameManager().getStatus();
            if (gameStatus.equals(GameStatus.GRACE) || gameStatus.equals(GameStatus.STARTED)) {
                this.instance.getGameManager().broadcastInGame(formatAll(this.instance.getLanguageManager().getConfig().getString("Game.Player Died")
                        .replace("%player_name%", player.getName())
                        .replace("%current_count%", String.valueOf(this.instance.getPlayers().size()))));
            }
        }

        if (this.instance.getSpectators().contains(player))
            this.instance.getGameManager().removeSpectator(player);
    }
}

