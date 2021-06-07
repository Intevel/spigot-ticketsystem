package me.exception.tickets.manager;

import com.google.common.collect.Lists;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import me.exception.tickets.SystemMain;
import me.exception.tickets.entities.TicketMessage;
import me.exception.tickets.mysql.AsyncMySQL;
import me.exception.tickets.utils.ItemBuilder;
import me.exception.tickets.utils.Skull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class MessageManager {
    private static AsyncMySQL asyncMySQL = SystemMain.getInstance().getAsyncMySQL();

    public static void addAnswer(TicketMessage message) {
        try {
            PreparedStatement statement = asyncMySQL.prepare("INSERT INTO ticketAnswers(ticketId, sender, message) VALUES (?,?,?)");
            statement.setInt(1, message.getTicketId());
            statement.setString(2, message.getSender());
            statement.setString(3, message.getMessage());
            asyncMySQL.update(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTicketAnwsers(int id, Consumer<List<TicketMessage>> listConsumer) {
        List<TicketMessage> answers = Lists.newArrayList();
        asyncMySQL.query("SELECT * FROM ticketAnswers WHERE ticketId = '" + id + "';", resultSet -> {
            try {
                if (resultSet == null) {
                    return;
                }

                while (resultSet.next()) {
                    TicketMessage ticketMessage = new TicketMessage(resultSet.getInt("ticketId"), resultSet.getString("sender"), resultSet.getString("message"));

                    answers.add(ticketMessage);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            listConsumer.accept(answers);
        });
    }


    public static void onChatGUI(int id, Player p) {
        SystemMain.getInstance();
        SystemMain.getMessageManager().getTicketAnwsers(id, ticketMessages -> {
            Inventory inv = Bukkit.createInventory(null, 27, "§8» §aNachrichtenverlauf #" + id);
            ticketMessages.forEach(message -> inv.addItem(new ItemStack[]{new ItemBuilder(Material.PAPER).setDisplayName("§8» §aNachricht").addLoreLine("§8§m§l----------------").addLoreLine("§8§l\u279c §7Von: §a" + message.getMessage()).addLoreLine("§8§l\u279c §7Nachricht: §a" + message.getSender()).addLoreLine("§8§m§l----------------").build()}));

            ItemStack skull_1 = Skull.getCustomSkull("http://textures.minecraft.net/texture/8a314b65d3959ef15b8a36436fdc9ae804f381b478debc778c40ffbb02fbcddf");
            ItemMeta meta = skull_1.getItemMeta();
            meta.setDisplayName("§8» §aNachricht schreiben");
            skull_1.setItemMeta(meta);
            inv.setItem(26, skull_1);
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            p.openInventory(inv);
        });
    }
}

    