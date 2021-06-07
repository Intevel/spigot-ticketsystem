
package me.exception.tickets;

import me.exception.tickets.commands.TicketsCommand;
import me.exception.tickets.listener.ClickListener;
import me.exception.tickets.listener.JoinListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import me.exception.tickets.mysql.AsyncMySQL;
import me.exception.tickets.manager.MessageManager;
import me.exception.tickets.mysql.TicketManagerSQL;
import me.exception.tickets.manager.InventoryManager;
import me.exception.tickets.manager.TicketManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemMain extends JavaPlugin
{
    private static SystemMain instance;
    private static TicketManager ticketManager;
    private static InventoryManager inventoryManager;
    private static TicketManagerSQL ticketManagerSQL;
    private static MessageManager messageManager;
    private static String prefix;
    private AsyncMySQL asyncMySQL;
    public String host;
    public String user;
    public String db;
    public String password;
    public int port;
    
    public SystemMain() {
        this.host = this.getConfig().getString("MySQL.host");
        this.user = this.getConfig().getString("MySQL.username");
        this.db = this.getConfig().getString("MySQL.datenbank");
        this.password = this.getConfig().getString("MySQL.passwort");
        this.port = this.getConfig().getInt("MySQL.port");
    }
    
    public void onEnable() {
        SystemMain.instance = this;
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        SystemMain.ticketManager = new TicketManager();
        SystemMain.inventoryManager = new InventoryManager();
        this.getCommand("tickets").setExecutor((CommandExecutor)new TicketsCommand());
        Bukkit.getPluginManager().registerEvents((Listener)new InventoryManager(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new JoinListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new ClickListener(), (Plugin)this);
        this.asyncMySQL = new AsyncMySQL((Plugin)this, this.host, this.port, this.user, this.password, this.db);
        SystemMain.ticketManagerSQL = new TicketManagerSQL();
        SystemMain.messageManager = new MessageManager();
        this.createTicketTable();
        this.createTicketMessageTable();
    }
    
    private void createTicketTable() {
        this.asyncMySQL.update("CREATE TABLE IF NOT EXISTS `tickets` (`id` int(255) NOT NULL, `name` varchar(255) NOT NULL, `sender` varchar(255) NOT NULL, `category` varchar(255) NOT NULL, `message` varchar(255) NOT NULL,`state` enum('PENDING','PROCESSING','FINISH','') NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1");
    }
    
    private void createTicketMessageTable() {
        this.asyncMySQL.update("CREATE TABLE IF NOT EXISTS `ticketAnswers` (`ticketId` int(255) NOT NULL,`message` varchar(255) NOT NULL,`sender` varchar(255) NOT NULL,`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP) ENGINE=InnoDB DEFAULT CHARSET=latin1");
    }
    
    public void onDisable() {
    }
    
    public static SystemMain getInstance() {
        return SystemMain.instance;
    }
    
    public static TicketManager getTicketManager() {
        return SystemMain.ticketManager;
    }
    
    public AsyncMySQL getAsyncMySQL() {
        return this.asyncMySQL;
    }
    
    public static TicketManagerSQL getTicketManagerSQL() {
        return SystemMain.ticketManagerSQL;
    }
    
    public static InventoryManager getInventoryManager() {
        return SystemMain.inventoryManager;
    }
    
    public static String getPrefix() {
        return getInstance().getConfig().getString("Nachrichten.prefix").replaceAll("&", "§");
    }
    
    public static MessageManager getMessageManager() {
        return SystemMain.messageManager;
    }
}
