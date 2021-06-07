

package me.exception.tickets.listener;

import java.util.HashMap;
import java.util.HashSet;

import me.exception.tickets.entities.TicketMessage;
import me.exception.tickets.entities.TicketState;
import me.exception.tickets.manager.MessageManager;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import me.exception.tickets.manager.InventoryManager;
import me.exception.tickets.SystemMain;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Map;
import org.bukkit.entity.Player;
import java.util.Set;
import org.bukkit.event.Listener;

public class ClickListener implements Listener {

    public static Set<Player> ticketMessage;
    public static Set<Player> ticketKategorie;
    public static Set<Player> ticketNamer;
    public static Map<Player, Integer> anwser;
    private String name;
    private String kategorie;
    private String message;
    
    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getClickedInventory().getName().equalsIgnoreCase("§8» §aTicket-Übersicht")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName() == "§8» §aTicket erstellen") {
                p.sendMessage(SystemMain.getPrefix() + "§7Schreibe nun deinen Ticket Namen in den Chat!");
                p.closeInventory();
                ClickListener.ticketNamer.add(p);
            }
            else if (e.getCurrentItem().getItemMeta().getDisplayName() == "§8» §2Deine Tickets anzeigen") {
                InventoryManager.openOwnTickets(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (e.getCurrentItem().getItemMeta().getDisplayName() == "§8» §9Alle Tickets anzeigen") {
                InventoryManager.openScammers(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
        else if (e.getClickedInventory().getName().startsWith("§8» §aNachrichtenverlauf #")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName() == "§8» §aNachricht schreiben") {
                p.sendMessage(SystemMain.getPrefix() + SystemMain.getInstance().getConfig().getString("Nachrichten.writeYourMessageInChat").replaceAll("&", "§"));
                p.closeInventory();
                ClickListener.anwser.put(p, Integer.parseInt(e.getClickedInventory().getName().replace("§8» §aNachrichtenverlauf #", "")));
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        if (ClickListener.ticketNamer.contains(e.getPlayer())) {
            e.setCancelled(true);
            if (e.getMessage().length() <= 100) {
                this.name = e.getMessage();
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Bitte gebe nun dein Problem in den Chat ein!");
            }
            else {
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Die Nachricht ist zu lang! (Maximal 100 Zeichen)");
            }
            ClickListener.ticketNamer.remove(e.getPlayer());
            ClickListener.ticketMessage.add(e.getPlayer());
        }
        else if (ClickListener.ticketMessage.contains(e.getPlayer())) {
            e.setCancelled(true);
            if (e.getMessage().length() <= 100) {
                this.message = e.getMessage();
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Bitte gebe nun die Kategorie in den Chat ein!");
            }
            else {
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Die Nachricht ist zu lang! (Maximal 100 Zeichen)");
            }
            ClickListener.ticketMessage.remove(e.getPlayer());
            ClickListener.ticketKategorie.add(e.getPlayer());
        }
        else if (ClickListener.ticketKategorie.contains(e.getPlayer())) {
            e.setCancelled(true);
            if (e.getMessage().length() <= 100) {
                this.kategorie = e.getMessage();
                SystemMain.getTicketManagerSQL().addTicket(this.name, e.getPlayer().getName(), this.message, this.kategorie, TicketState.PENDING);
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Du hast erfolgreich ein neues §aTicket §7erstellt");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else {
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Die Nachricht ist zu lang! (Maximal 100 Zeichen)");
            }
            ClickListener.ticketKategorie.remove(e.getPlayer());
        }
        else if (ClickListener.anwser.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            if (e.getMessage().length() <= 100) {
                MessageManager.addAnswer(new TicketMessage(ClickListener.anwser.get(e.getPlayer()), e.getMessage(), e.getPlayer().getName()));
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Du hast erfolgreich eine neue Nachricht geschrieben!");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else {
                e.getPlayer().sendMessage(SystemMain.getPrefix() + "§7Die Nachricht ist zu lang! (Maximal 100 Zeichen)");
            }
            ClickListener.anwser.remove(e.getPlayer());
        }
    }
    
    static {
        ClickListener.ticketMessage = new HashSet<Player>();
        ClickListener.ticketKategorie = new HashSet<Player>();
        ClickListener.ticketNamer = new HashSet<Player>();
        ClickListener.anwser = new HashMap<Player, Integer>();
    }
}
