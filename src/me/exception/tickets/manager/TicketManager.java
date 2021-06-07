package me.exception.tickets.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.exception.tickets.SystemMain;
import me.exception.tickets.entities.Ticket;
import me.exception.tickets.entities.TicketState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class TicketManager {
    private Map<Integer, TicketState> ticketStateMap;
    private static Map<Integer, Ticket> ticketMap;
    private static List<Ticket> tickets;
    private static File file;
    private static YamlConfiguration configuration;

    public TicketManager() {
        file = new File(SystemMain.getInstance().getDataFolder(), "tickets.yml");
        configuration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) try {
            file.createNewFile();
            configuration.set("tickets", new ArrayList());
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ticketMap = new HashMap<>();
        tickets = new ArrayList<>();
        this.ticketStateMap = new HashMap<>();
        configuration.getStringList("tickets").forEach(s -> {
            tickets.add(Ticket.fromString(s));
            String[] split = s.split("#");
            this.ticketStateMap.put(Integer.valueOf(Integer.parseInt(split[0])), TicketState.valueOf(split[5]));
            Ticket value = new Ticket(Integer.parseInt(split[0]), split[1], split[2], split[3], split[4], TicketState.valueOf(split[5]));
            ticketMap.put(Integer.valueOf(Integer.parseInt(split[0])), value);
        });
    }


    public static void save() {
        List<String> rawData = new ArrayList<>();
        if (tickets.size() == 0) {
            configuration.set("tickets", rawData);
            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        tickets.forEach(ticket -> {
            System.out.println(ticket.getId());

            rawData.add(ticket.toString());
        });
        configuration.set("tickets", rawData);
    }

    public void addTicket(String ticketName, Player ticketSender, String ticketMessage, String kategorie, TicketState status) {
        Ticket e = new Ticket(0, ticketName, ticketSender.getName(), ticketMessage, kategorie, status);
        tickets.add(e);
        getTicketMap().put(Integer.valueOf(e.getId()), e);
        getTicketStateMap().put(Integer.valueOf(e.getId()), TicketState.PENDING);
        save();
    }

    public void removeTicket(int id) {
        try {
            Ticket ticket = ticketMap.get(Integer.valueOf(id));
            if (ticket == null) {
                return;
            }
            tickets.remove(ticket);
            this.ticketStateMap.remove(Integer.valueOf(id));
            ticketMap.remove(Integer.valueOf(id));
            save();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public Map<Integer, TicketState> getTicketStateMap() {
        return this.ticketStateMap;
    }

    public void setTicketStateMap(Map<Integer, TicketState> ticketStateMap) {
        this.ticketStateMap = ticketStateMap;
    }

    public void setTicketMap(Map<Integer, Ticket> ticketMap) {
        TicketManager.ticketMap = ticketMap;
    }

    public void setTickets(List<Ticket> tickets) {
        TicketManager.tickets = tickets;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        TicketManager.file = file;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(YamlConfiguration configuration) {
        TicketManager.configuration = configuration;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Map<Integer, Ticket> getTicketMap() {
        return ticketMap;
    }
}
