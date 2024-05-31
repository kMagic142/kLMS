package ro.kmagic.klms.game;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.event.PlayerUntagEvent;
import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.settings.InventoryManager;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.PlayerUtils;
import ro.kmagic.klms.utils.SerializationUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public class GameManager implements MessageUtils, SerializationUtils, PlayerUtils {
    private final KLMS instance;
    private final FileConfiguration settings;
    private final FileConfiguration language;
    private GameStatus status;
    private int timer;
    private int stopTimer;
    private long startTime;
    private int currentTaskID;
    private final Sound NOTE_SOUND;

    public GameManager(KLMS instance) {
        this.instance = instance;

        this.settings = instance.getSettingsManager().getConfig();
        this.language = instance.getLanguageManager().getConfig();

        this.status = GameStatus.IDLE;
        this.stopTimer = -1;


        Sound clickSound = null;
        String[] clickSounds = { "BLOCK_NOTE_BLOCK_XYLOPHONE", "NOTE_PIANO" };
        for (String s : clickSounds) {
            try {
                clickSound = Sound.valueOf(s.toUpperCase());
                break;
            } catch (IllegalArgumentException illegalArgumentException) {}
        }
        this.NOTE_SOUND = clickSound;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void waitForPlayers() {
        this.status = GameStatus.WAITING;
        this.timer = this.settings.getInt("Game.Starting Counter");

        if (this.settings.getBoolean("Arena Auto-Stop.Enable")) {
            this.stopTimer = this.settings.getInt("Arena Auto-Stop.After X Seconds");
        }

        this.currentTaskID = this.instance.getServer().getScheduler().scheduleAsyncRepeatingTask((Plugin)this.instance, (Runnable)new BukkitRunnable() {
            private int announceTimer = 30;

            public void run() {
                if (GameManager.this.status.equals(GameStatus.IDLE)) {
                    GameManager.this.cancelCurrentTask();
                }

                if (GameManager.this.stopTimer == 0) {
                    GameManager.this.endGame();
                }
                if (GameManager.this.stopTimer != -1) {
                    --GameManager.this.stopTimer;
                }

                if (GameManager.this.status.equals(GameStatus.WAITING) && this.announceTimer++ == 30) {
                    this.announceTimer = 0;
                    Bukkit.broadcastMessage(GameManager.this.formatAll(GameManager.this.instance.getLanguageManager().getConfig().getString("Game.Waiting For Players")
                            .replace("%current_count%", String.valueOf(GameManager.this.instance.getPlayers().size()))
                            .replace("%max_count%", String.valueOf(GameManager.this.settings.getInt("Game.Maximum Player Count")))));
                }

                if (GameManager.this.status.equals(GameStatus.STARTING)) {
                    if (GameManager.this.timer == 0) {
                        GameManager.this.startGame();
                    } else if (GameManager.this.timer <= 10) {
                        GameManager.this.soundInGame(GameManager.this.NOTE_SOUND);
                        Bukkit.broadcastMessage(GameManager.this.formatAll(GameManager.this.instance.getLanguageManager().getConfig().getString("Game.Starting In")
                                .replace("%time%", GameManager.this.convertTime((GameManager.this.timer * 1000), GameManager.this.language))
                                .replace("%current_count%", String.valueOf(GameManager.this.instance.getPlayers().size()))
                                .replace("%max_count%", String.valueOf(GameManager.this.settings.getInt("Game.Maximum Player Count")))));
                    } else if (GameManager.this.timer % 30 == 0) {
                        Bukkit.broadcastMessage(GameManager.this.formatAll(GameManager.this.instance.getLanguageManager().getConfig().getString("Game.Starting In")
                                .replace("%time%", GameManager.this.convertTime((GameManager.this.timer * 1000), GameManager.this.language))
                                .replace("%current_count%", String.valueOf(GameManager.this.instance.getPlayers().size()))
                                .replace("%max_count%", String.valueOf(GameManager.this.settings.getInt("Game.Maximum Player Count")))));
                    }
                    --GameManager.this.timer;
                }
            }
        },20L, 20L);
    }

    public int getTimer() {
        return timer;
    }

    public void startGame() {
        cancelCurrentTask();

        this.status = this.settings.getBoolean("Game.Grace Period.Enabled") ? GameStatus.GRACE : GameStatus.STARTED;
        this.startTime = System.currentTimeMillis();

        try {
            Location arenaLocation = deserializeLocation(this.settings.getString("Game.Locations.Arena"));
            ItemStack[] contents = itemStackArrayFromBase64(this.settings.getString("Game.Kit.Inventory"));
            ItemStack[] armorContents = itemStackArrayFromBase64(this.settings.getString("Game.Kit.Armor"));

            BukkitScheduler scheduler = this.instance.getServer().getScheduler();
            this.instance.getPlayers().forEach(player -> {
                IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(essentials.getUser(player).hasOutstandingTeleportRequest()) {
                            essentials.getUser(essentials.getUser(player).getTeleportRequest()).getAsyncTeleport().back(getNewExceptionFuture());
                        }

                        player.teleport(arenaLocation);

                        resetPlayerData(player);
                        player.getInventory().setContents(contents);
                        player.getInventory().setArmorContents(armorContents);
                        player.updateInventory();
                        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                    }
                }.runTask(instance);
            });
            this.instance.getSpectators().forEach(player -> player.teleport(arenaLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        broadcastInGame(formatAll(this.instance.getLanguageManager().getConfig().getString("Game.Started")));

        if (this.status.equals(GameStatus.GRACE)) {
            this.currentTaskID = this.instance.getServer().getScheduler().scheduleAsyncRepeatingTask(this.instance, new BukkitRunnable() {
                private int graceTimer = GameManager.this.settings.getInt("Game.Grace Period.Time In Seconds");


                public void run() {
                    if (GameManager.this.status.equals(GameStatus.IDLE)) {
                        GameManager.this.cancelCurrentTask();
                    }
                    if (this.graceTimer == 0) {
                        GameManager.this.status = GameStatus.STARTED;
                        GameManager.this.broadcastInGame(GameManager.this.formatAll(GameManager.this.instance.getLanguageManager().getConfig().getString("Game.Grace Period Ended")));
                        GameManager.this.cancelCurrentTask();
                    } else if (this.graceTimer <= 10) {
                        GameManager.this.soundInGame(GameManager.this.NOTE_SOUND);
                        GameManager.this.broadcastInGame(GameManager.this.formatAll(GameManager.this.instance.getLanguageManager().getConfig().getString("Game.Grace Period Ending")
                                .replace("%time%", GameManager.this.convertTime((this.graceTimer * 1000), GameManager.this.language))));
                        this.graceTimer--;
                    } else if (this.graceTimer % 30 == 0) {
                        GameManager.this.broadcastInGame(GameManager.this.formatAll(GameManager.this.instance.getLanguageManager().getConfig().getString("Game.Grace Period Ending")
                                .replace("%time%", GameManager.this.convertTime((this.graceTimer * 1000), GameManager.this.language))));
                        this.graceTimer--;
                    }
                }
            },20L, 20L);
        }
    }

    public void endGame() {
        Player winner = this.instance.getPlayers().get(0);

        Bukkit.broadcastMessage(formatAll(this.language.getString("Game.Finished")
                .replace("%winner%", winner.getName())
                .replace("%time%", convertTime(System.currentTimeMillis() - this.startTime, this.language))));

        removePlayer(winner);
        removeAllSpectators();

        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

        for (String command : this.settings.getStringList("Game.Winner Rewards")) {
            Bukkit.dispatchCommand(consoleCommandSender, command.replace("%player_name%", winner.getName()));
        }
    }

    public void forceEndGame() {
        this.status = GameStatus.IDLE;

        removeAllPlayers();
        removeAllSpectators();

        cancelCurrentTask();
    }

    public void addPlayer(Player player) {
        this.instance.getPlayers().add(player);

        InventoryManager inventoryManager = this.instance.getInventoryManager();
        savePlayerData(player, inventoryManager);
        resetPlayerData(player);

        if (this.status.equals(GameStatus.WAITING) && this.instance.getPlayers().size() >= this.settings.getInt("Game.Minimum Player Count")) {
            this.status = GameStatus.STARTING;
        }

        player.teleport(deserializeLocation(this.settings.getString("Game.Locations.Lobby")));

        broadcastInGame(formatAll(this.instance.getLanguageManager().getConfig().getString("Game.Waiting.Player Joined")
                .replace("%player_name%", player.getName())
                .replace("%current_count%", String.valueOf(this.instance.getPlayers().size()))
                .replace("%max_count%", String.valueOf(this.settings.getInt("Game.Maximum Player Count")))));
    }

    public void removePlayer(Player player) {
        if (!this.instance.getPlayers().contains(player)) {
            return;
        }

        this.instance.getPlayers().remove(player);
        int playerCount = this.instance.getPlayers().size();

        if (player.isOnline()) {
            resetPlayerData(player);
            player.teleport(deserializeLocation(this.settings.getString("Game.Locations.Spawn")));
            loadPlayerData(player, this.instance.getInventoryManager());

            if (Bukkit.getServer().getPluginManager().isPluginEnabled("CombatLogX")) {
                ICombatLogX plugin = (ICombatLogX)Bukkit.getPluginManager().getPlugin("CombatLogX");
                plugin.getCombatManager().untag(player, PlayerUntagEvent.UntagReason.EXPIRE);
            }
        }

        if (this.status.equals(GameStatus.STARTING) && playerCount < this.settings.getInt("Game.Minimum Player Count")) {
            this.status = GameStatus.WAITING;
            this.timer = this.settings.getInt("Game.Starting Counter");
        }

        if (this.status.equals(GameStatus.WAITING) || this.status.equals(GameStatus.STARTING)) {
            broadcastInGame(formatAll(this.instance.getLanguageManager().getConfig().getString("Game.Waiting.Player Quit")
                    .replace("%player_name%", player.getName())
                    .replace("%current_count%", String.valueOf(this.instance.getPlayers().size()))
                    .replace("%max_count%", String.valueOf(this.settings.getInt("Game.Maximum Player Count")))));

            return;
        }
        if (!this.status.equals(GameStatus.IDLE)) {
            if (playerCount == 1) {
                endGame();
            } else if (playerCount < 1) {
                this.status = GameStatus.IDLE;
            }
        }
    }

    public void removeAllPlayers() {
        InventoryManager inventoryManager = this.instance.getInventoryManager();
        ICombatLogX plugin = (ICombatLogX)Bukkit.getPluginManager().getPlugin("CombatLogX");
        Location location = deserializeLocation(this.settings.getString("Game.Locations.Spawn"));

        for (Iterator<Player> iterator = this.instance.getPlayers().iterator(); iterator.hasNext(); ) {
            Player player = iterator.next();

            resetPlayerData(player);
            Bukkit.getScheduler().runTask(instance, () -> player.teleport(location));
            loadPlayerData(player, inventoryManager);

            if (Bukkit.getServer().getPluginManager().isPluginEnabled("CombatLogX")) {
                plugin.getCombatManager().untag(player, PlayerUntagEvent.UntagReason.EXPIRE);
            }

            iterator.remove();
        }
    }

    public void addSpectator(Player player) {
        this.instance.getSpectators().add(player);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        Bukkit.getOnlinePlayers().forEach(targetPlayer -> targetPlayer.hidePlayer(player));
        if (this.status.equals(GameStatus.WAITING) || this.status.equals(GameStatus.STARTING)) {
            player.teleport(deserializeLocation(this.settings.getString("Game.Locations.Lobby")));
        } else {
            player.teleport(deserializeLocation(this.settings.getString("Game.Locations.Arena")));
        }
    }

    public void removeSpectator(Player player) {
        this.instance.getSpectators().remove(player);

        player.teleport(deserializeLocation(this.settings.getString("Game.Locations.Spawn")));
        Bukkit.getOnlinePlayers().forEach(targetPlayer -> targetPlayer.showPlayer(player));
    }

    public void removeAllSpectators() {
        for (Iterator<Player> iterator = this.instance.getSpectators().iterator(); iterator.hasNext(); ) {
            Player spectator = iterator.next();
            spectator.teleport(deserializeLocation(this.settings.getString("Game.Locations.Spawn")));
            Bukkit.getOnlinePlayers().forEach(targetPlayer -> targetPlayer.showPlayer(spectator));

            iterator.remove();
        }
    }

    public void broadcastInGame(String message) {
        for (Player player : this.instance.getPlayers()) {
            player.sendMessage(message);
        }

        for (Player player : this.instance.getSpectators()) {
            player.sendMessage(message);
        }
    }

    private void soundInGame(Sound sound) {
        for (Player player : this.instance.getPlayers()) {
            player.playSound(player.getEyeLocation(), sound, 1.0F, 1.0F);
        }

        for (Player player : this.instance.getSpectators()) {
            player.playSound(player.getEyeLocation(), sound, 1.0F, 1.0F);
        }
    }

    private void cancelCurrentTask() {
        Bukkit.getScheduler().cancelTask(this.currentTaskID);
    }

    public CompletableFuture<Boolean> getNewExceptionFuture() {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.exceptionally(e -> false);
        return future;
    }
}

