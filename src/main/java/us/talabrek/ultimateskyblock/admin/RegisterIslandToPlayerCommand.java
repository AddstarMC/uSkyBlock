package us.talabrek.ultimateskyblock.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import us.talabrek.ultimateskyblock.command.AbstractUSBCommand;
import us.talabrek.ultimateskyblock.player.PlayerInfo;
import us.talabrek.ultimateskyblock.uSkyBlock;

import java.util.Map;

/**
 * Registers an island to a player.
 */
public class RegisterIslandToPlayerCommand extends AbstractUSBCommand {
    public RegisterIslandToPlayerCommand() {
        super("register", "usb.admin.register", "player", "set a player's island to your location");
    }
    @Override
    public boolean execute(CommandSender sender, Map<String,Object> data, String... args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        String playerName = args[0];
        Player player = (Player) sender;
        if (uSkyBlock.getInstance().devSetPlayerIsland(player, player.getLocation(), playerName)) {
            sender.sendMessage(ChatColor.GREEN + "Set " + playerName + "'s island to the bedrock nearest you.");
        } else {
            sender.sendMessage(ChatColor.RED + "Bedrock not found: unable to set the island!");
        }
        return true;
    }
}
