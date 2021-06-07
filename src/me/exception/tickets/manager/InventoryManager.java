package me.exception.tickets.manager;
/*     */

import me.exception.tickets.SystemMain;
import me.exception.tickets.entities.TicketState;
import me.exception.tickets.utils.ItemBuilder;
import me.exception.tickets.utils.Skull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManager implements Listener {

    public static void openMainInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8» §aTicket-Übersicht");

        ItemStack skull_1 = Skull.getCustomSkull("http://textures.minecraft.net/texture/2ef2d4273700874f1593d21e1681f461735f8b07ddd42967293f0df713ffa929");
        ItemMeta meta = skull_1.getItemMeta();
        meta.setDisplayName("§8» §aTicket erstellen");
        skull_1.setItemMeta(meta);

        ItemStack skull_2 = Skull.getCustomSkull("http://textures.minecraft.net/texture/35747010d84aa56483b75f6243d94f34c534603584b2cc68a45bf365840105fd");
        ItemMeta met2a = skull_2.getItemMeta();
        met2a.setDisplayName("§8» §2Deine Tickets anzeigen");
        skull_2.setItemMeta(met2a);

        ItemStack skull_3 = Skull.getCustomSkull("http://textures.minecraft.net/texture/16439d2e306b225516aa9a6d007a7e75edd2d5015d113b42f44be62a517e574f");
        ItemMeta met3a = skull_3.getItemMeta();
        met3a.setDisplayName("§8» §9Alle Tickets anzeigen");
        skull_3.setItemMeta(met3a);

        if (player.hasPermission("ticketsystem.viewall.tickets")) {
            inventory.setItem(11, skull_1);
            inventory.setItem(13, skull_3);
            inventory.setItem(15, skull_2);
        } else {
            inventory.setItem(11, skull_1);
            inventory.setItem(15, skull_2);
        }

        player.openInventory(inventory);
    }


    public static void openScammers(Player player) {
        SystemMain.getTicketManagerSQL().getTickets(tickets -> {
            Inventory inventory = Bukkit.createInventory(null, 45, "§8» §aTickets");
            tickets.forEach(ticket -> inventory.addItem(new ItemStack[] { new ItemBuilder(Material.PAPER).setDisplayName("§8» §a§l" + ticket.getTicketName() + " #" + ticket.getId()).addLoreLine("§8§m§l----------------").addLoreLine("§8§l\u279c §7Von: §a" + ticket.getTicketSender()).addLoreLine("§8§l\u279c §7Kategorie: §a" + ticket.getKategorie()).addLoreLine("§8§l\u279c §7Status: " + ticket.getStatus().getName()).addLoreLine("§8§m§l----------------").build() }));
            player.openInventory(inventory);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        });
    }


    public static void openOwnTickets(Player player) {
        final Inventory inventory2 = null;
        SystemMain.getTicketManagerSQL().getTickets(tickets -> {
            Inventory inventory = Bukkit.createInventory(null, 45, "§8» §aDeine Tickets");
            tickets.forEach(ticket -> {
                if (ticket.getTicketSender().equalsIgnoreCase(player.getName())) {
                    inventory.addItem(new ItemStack[] { new ItemBuilder(Material.PAPER).setDisplayName("§8» §a§l" + ticket.getTicketName() + " #" + ticket.getId()).addLoreLine("§8§m§l----------------").addLoreLine("§8§l\u279c §7Von: §a" + ticket.getTicketSender()).addLoreLine("§8§l\u279c §7Kategorie: §a" + ticket.getKategorie()).addLoreLine("§8§l\u279c §7Status: " + ticket.getStatus().getName()).addLoreLine("§8§m§l----------------").build() });
                }
                return;
            });
            player.openInventory(inventory);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        });
    }


    public static void openTicketInv(Player player, int id) {
        SystemMain.getTicketManagerSQL().getTicketById(id, ticket -> {
            Inventory inventory = Bukkit.createInventory(null, 27, "§8» §aTicket #" + ticket.getId());
            ItemStack skull_1 = Skull.getCustomSkull("http://textures.minecraft.net/texture/f32c1472a7bc6975ded7c0c51696959b89af61b75ae954cc4036bc384b3b8301");
            ItemMeta meta = skull_1.getItemMeta();
            meta.setDisplayName("§8» §7Status ändern");
            skull_1.setItemMeta(meta);
            ItemStack skull_2 = Skull.getCustomSkull("http://textures.minecraft.net/texture/20a7bdecdd6433e8008b7c1e3bfd8e4f696cb343afbb2049a964efba4df5c7cb");
            ItemMeta meta2 = skull_2.getItemMeta();
            meta2.setDisplayName("§7» §aNachrichtenverlauf");
            skull_2.setItemMeta(meta2);
            inventory.setItem(10, skull_1);
            inventory.setItem(12, skull_2);
            inventory.setItem(14, (new ItemBuilder(Material.PAPER)).setDisplayName("§7➜ §7Informationen").addLoreLine("§8§m§l----------------").addLoreLine("§8§l➜ §7Nachricht: §a" + ticket.getTicketMessage()).addLoreLine("§8§l➜ §7Von: §a" + ticket.getTicketSender()).addLoreLine("§8§l➜ §7Kategorie: §a" + ticket.getKategorie()).addLoreLine("§8§m§l----------------").build());
            inventory.setItem(16, (new ItemBuilder(Material.BARRIER)).setDisplayName("§8§l» §7Löschen").build());
            player.openInventory(inventory);
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        });
    }


    public static void openTicketInv2(Player player, int id) {
        SystemMain.getTicketManagerSQL().getTicketById(id, ticket -> {
            Inventory inventory = Bukkit.createInventory(null, 27, "§8» §aTicket #" + ticket.getId());
            ItemStack skull_2 = Skull.getCustomSkull("http://textures.minecraft.net/texture/20a7bdecdd6433e8008b7c1e3bfd8e4f696cb343afbb2049a964efba4df5c7cb");
            ItemMeta meta2 = skull_2.getItemMeta();
            meta2.setDisplayName("§7» §aNachrichtenverlauf");
            skull_2.setItemMeta(meta2);
            inventory.setItem(11, skull_2);
            inventory.setItem(13, (new ItemBuilder(Material.PAPER)).setDisplayName("§7➜ §7Informationen").addLoreLine("§8§m§l----------------").addLoreLine("§8§l➜ §7Problem: §a" + ticket.getTicketMessage()).addLoreLine("§8§l➜ §7Kategorie: §a" + ticket.getKategorie()).addLoreLine("§8§l➜ §7Status: " + ticket.getStatus().getName()).addLoreLine("§8§m§l----------------").build());
            inventory.setItem(15, (new ItemBuilder(Material.BARRIER)).setDisplayName("§8§l» §7Löschen").build());
            player.openInventory(inventory);
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        });
    }


    public static void openChangeTicketState(Player player, int id) {
        SystemMain.getTicketManagerSQL().getTicketById(id, ticket -> {
            if (ticket != null) {
                ItemStack skull_1 = Skull.getCustomSkull("http://textures.minecraft.net/texture/533a5bfc8a2a3a152d646a5bea694a425ab79db694b214f156c37c7183aa");
                ItemMeta meta = skull_1.getItemMeta();
                meta.setDisplayName("§8» §7" + TicketState.PENDING.getName());
                skull_1.setItemMeta(meta);
                ItemStack skull_2 = Skull.getCustomSkull("http://textures.minecraft.net/texture/41139b3ef2e4c44a4c983f114cbe948d8ab5d4f879a5c665bb820e7386ac2f");
                ItemMeta meta2 = skull_2.getItemMeta();
                meta2.setDisplayName("§8» §7" + TicketState.PROCESSING.getName());
                skull_2.setItemMeta(meta2);
                ItemStack skull_3 = Skull.getCustomSkull("http://textures.minecraft.net/texture/85484f4b6367b95bb16288398f1c8dd6c61de988f3a8356d4c3ae73ea38a42");
                ItemMeta meta3 = skull_3.getItemMeta();
                meta3.setDisplayName("§8» §7" + TicketState.FINISH.getName());
                skull_3.setItemMeta(meta3);
                Inventory inventory = Bukkit.createInventory(null, 27, "§8» §aTicketstatus #" + ticket.getId());
                inventory.setItem(11, skull_1);
                inventory.setItem(13, skull_2);
                inventory.setItem(15, skull_3);
                player.openInventory(inventory);
            } else {
                player.sendMessage(SystemMain.getPrefix() + SystemMain.getInstance().getConfig().getString("Nachrichten.ticketNotFound").replaceAll("&", "§"));
            }
        });
    }


    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equalsIgnoreCase("§8» §aTickets")) {

            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8»")) {
                String[] strings = event.getCurrentItem().getItemMeta().getDisplayName().split("#");

                int id = Integer.parseInt(strings[1].replace(" ", ""));


                openTicketInv(player, id);
            }

        } else if (event.getInventory().getName().equalsIgnoreCase("§8» §aDeine Tickets")) {

            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8»")) {

                String[] strings = event.getCurrentItem().getItemMeta().getDisplayName().split("#");

                int id = Integer.parseInt(strings[1].replace(" ", ""));


                openTicketInv2(player, id);
            }
        }


        if (event.getInventory().getName().startsWith("§8» §aTicket #")) {

            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8§l» §7Löschen")) {
                String[] strings = event.getInventory().getName().split("#");
                int id = Integer.parseInt(strings[1]);
                SystemMain.getTicketManagerSQL().removeTicket(id);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                player.closeInventory();

                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8» §7Status ändern")) {
                String[] strings = event.getInventory().getName().split("#");

                int id = Integer.parseInt(strings[1]);

                openChangeTicketState(player, id);
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7» §aNachrichtenverlauf")) {
                String[] strings = event.getInventory().getName().split("#");

                int id = Integer.parseInt(strings[1]);

                MessageManager.onChatGUI(id, player);
            }
        }


        if (event.getInventory().getName().startsWith("§8» §aTicketstatus #")) {

            String[] strings = event.getInventory().getName().split("#");

            int id = Integer.parseInt(strings[1]);


            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8» §7")) {
                for (TicketState ticketState : TicketState.values()) {

                    String state = event.getCurrentItem().getItemMeta().getDisplayName().replace("§8» §7", "");
                    if (ticketState.getName().equalsIgnoreCase(state)) {
                        player.sendMessage(SystemMain.getPrefix() + SystemMain.getInstance().getConfig().getString("Nachrichten.changeTicketStatus").replaceAll("&", "§"));
                        SystemMain.getTicketManagerSQL().changeState(id, TicketState.valueOf(ticketState.name()));

                        player.closeInventory();
                    }
                }
            }
        }
    }
}


