package ro.kmagic.klms.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.utils.MessageUtils;

public class PlayerDeathListener implements Listener, MessageUtils {
    KLMS instance;

    public PlayerDeathListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        if (this.instance.getPlayers().contains(player)) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            Player killer = player.getKiller();
            player.spigot().respawn();
            event.setDeathMessage(null);
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            fPlayer.setPowerRounded(fPlayer.getPowerRounded() + 4);

            if (killer != null) {
                this.instance.getGameManager().broadcastInGame(formatAll(this.instance.getLanguageManager().getConfig().getString("Game.Player Killed")
                        .replace("%player_name%", player.getName())
                        .replace("%player_killer%", killer.getName())
                        .replace("%current_count%", String.valueOf(this.instance.getPlayers().size()))));
                return;
            }
            this.instance.getGameManager().broadcastInGame(formatAll(this.instance.getLanguageManager().getConfig().getString("Game.Player Died")
                    .replace("%player_name%", player.getName())
                    .replace("%current_count%", String.valueOf(this.instance.getPlayers().size()))));
        }
    }
}

