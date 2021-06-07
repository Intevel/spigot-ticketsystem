

package me.exception.tickets.commands;

import me.exception.tickets.manager.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class TicketsCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        final Player p = (Player)commandSender;
        InventoryManager.openMainInv(p);
        return false;
    }
}
