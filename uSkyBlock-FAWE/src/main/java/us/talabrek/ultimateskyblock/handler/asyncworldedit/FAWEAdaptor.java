package us.talabrek.ultimateskyblock.handler.asyncworldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.talabrek.ultimateskyblock.player.PlayerPerk;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adaptor depending on FAWE (FastAsyncWorldEdit).
 */
public class FAWEAdaptor implements AWEAdaptor {
    private static final Logger log = Logger.getLogger(FAWEAdaptor.class.getName());
    private Plugin plugin;
    private int progressEveryMs;
    private double progressEveryPct;

    @Override
    public void onEnable(Plugin plugin) {
        this.plugin = plugin;
        progressEveryMs = plugin.getConfig().getInt("asyncworldedit.progressEveryMs", 3000);
        progressEveryPct = plugin.getConfig().getDouble("asyncworldedit.progressEveryPct", 20);
    }

    @Override
    public void onDisable(Plugin plugin) {
    }

    @Override
    public void registerCompletion(Player player) {
    }

    @Override
    public void loadIslandSchematic(final File file, final Location origin, final PlayerPerk playerPerk) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                boolean noAir = false;
                boolean entities = true;
                Vector to = new Vector(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
                EditSession editSession = createEditSession(BukkitUtil.getLocalWorld(origin.getWorld()), -1);
                try {
                    SchematicFormat.getFormat(file)
                            .load(file)
                            .paste(editSession, to, noAir, entities);
                    editSession.getQueue().setProgressTracker(new FAWEProgressTracker(playerPerk, progressEveryMs, progressEveryPct));
                    editSession.flushQueue();
                } catch (MaxChangedBlocksException | IOException | DataException e) {
                    log.log(Level.INFO, "Unable to paste schematic " + file, e);
                }
            }
        });
    }

    public EditSession createEditSession(World bukkitWorld, int maxBlocks) {
        return WorldEdit.getInstance().getEditSessionFactory().getEditSession(bukkitWorld, maxBlocks);
    }
}
