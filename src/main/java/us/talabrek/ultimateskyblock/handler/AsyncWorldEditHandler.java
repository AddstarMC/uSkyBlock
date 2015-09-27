package us.talabrek.ultimateskyblock.handler;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.talabrek.ultimateskyblock.handler.asyncworldedit.AsyncWorldEditAdaptor;
import us.talabrek.ultimateskyblock.player.PlayerPerk;
import us.talabrek.ultimateskyblock.uSkyBlock;

import java.io.File;

/**
 * Handles integration with AWE.
 * Very HACKY and VERY unstable.
 *
 * Only kept as a cosmetic measure, to at least try to give the players some feedback.
 */
public enum AsyncWorldEditHandler {;

    public static void onEnable(uSkyBlock plugin) {
        if (isAWE(plugin)) {
            AsyncWorldEditAdaptor.onEnable(plugin);
        }
    }

    public static void onDisable(uSkyBlock plugin) {
        if (isAWE(plugin)) {
            AsyncWorldEditAdaptor.onDisable(plugin);
        }
    }

    public static boolean isAWE(uSkyBlock plugin) {
        return Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit") && plugin.getConfig().getBoolean("asyncworldedit.enabled", true);
    }

    public static void registerCompletion(Player player) {
        if (isAWE(uSkyBlock.getInstance())) {
            AsyncWorldEditAdaptor.registerCompletion(player);
        }
    }

    public static EditSession createEditSession(BukkitWorld world, int maxblocks) {
        if (isAWE(uSkyBlock.getInstance())) {
            return AsyncWorldEditAdaptor.createSession(world, maxblocks);
        } else {
            return new EditSession(world, maxblocks);
        }
    }

    public static void flushQueue(final EditSession session) {
        if (session.getClass().getSimpleName().equals("AsyncEditSession")) {
            Bukkit.getScheduler().runTaskAsynchronously(uSkyBlock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    session.flushQueue();
                }
            });
        } else {
            session.flushQueue();
        }
    }

    public static void loadIslandSchematic(File file, Location origin, PlayerPerk playerPerk) {
        if (isAWE(uSkyBlock.getInstance())) {
            AsyncWorldEditAdaptor.loadIslandSchematic(file, origin, playerPerk);
        } else {
            WorldEditHandler.loadIslandSchematic(file, origin, playerPerk);
        }
    }
}
