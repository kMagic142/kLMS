package ro.kmagic.klms.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.game.GameStatus;

public class FoodLevelChangeListener implements Listener {
    KLMS instance;

    public FoodLevelChangeListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        HumanEntity humanEntity = event.getEntity();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        Player player = (Player)humanEntity;
        GameStatus gameStatus = this.instance.getGameManager().getStatus();
        if ((this.instance.getPlayers().contains(player) && (gameStatus.equals(GameStatus.WAITING) || gameStatus.equals(GameStatus.STARTING) || gameStatus.equals(GameStatus.GRACE))) || this.instance.getSpectators().contains(player)) {
            player.setFoodLevel(20);
            event.setCancelled(true);
        }
    }
}

