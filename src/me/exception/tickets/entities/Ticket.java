package me.exception.tickets.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.exception.tickets.SystemMain;


public class Ticket {

    private Ticket ticket;
    private int id;
    private String ticketName;
    private String ticketSender;
    private String ticketMessage;
    private String kategorie;
    private TicketState status;

    public Ticket(int id, String ticketName, String ticketSender, String ticketMessage, String kategorie, TicketState status) {
        this.id = id;
        this.ticketName = ticketName;
        this.ticketSender = ticketSender;
        this.ticketMessage = ticketMessage;
        this.kategorie = kategorie;
        this.status = status;
        this.ticket = this;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getTicketName() {
        return this.ticketName;
    }

    public String getTicketMessage() {
        return this.ticketMessage;
    }

    public void setTicketName(String name) {
        this.ticketName = name;
    }

    public String getTicketSender() {
        return this.ticketSender;
    }

    public String getKategorie() {
        return this.kategorie;
    }

    public void changeState(TicketState status) {
        SystemMain.getTicketManager().getTicketStateMap().remove(Integer.valueOf(getId()));
        SystemMain.getTicketManager().getTicketStateMap().put(Integer.valueOf(getId()), status);

        List<String> tickets = new ArrayList<>();
        SystemMain.getTicketManager().getConfiguration().getStringList("tickets").forEach(s -> {
            String[] list = s.split("#");
            if (Integer.parseInt(list[0]) == getId()) {
                for (TicketState state : TicketState.values()) {
                    s.replace(state.name(), status.name());
                }
                tickets.add(s);
            } else {
                tickets.add(s);
            }
        });
        List<String> ticketList = new ArrayList<>();
        for (Ticket ticket : SystemMain.getTicketManager().getTickets()) {
            ticketList.add(ticket.toString());
        }
        SystemMain.getTicketManager().getConfiguration().set("tickets", ticketList);
        try {
            SystemMain.getTicketManager().getConfiguration().save(SystemMain.getTicketManager().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String toString() {
        return this.id + "#" + this.ticketName + "#" + this.ticketSender + "#" + this.ticketMessage + "#" + this.kategorie + "#" + getStatus().name();
    }

    public static Ticket fromString(String data) {
        String[] splitData = data.split("#");
        return new Ticket(Integer.parseInt(splitData[0]), splitData[1], splitData[2], splitData[3], splitData[4], TicketState.valueOf(splitData[5]));
    }

    public void setTicketSender(String ticketSender) {
        this.ticketSender = ticketSender;
    }

    public void setTicketMessage(String ticketMessage) {
        this.ticketMessage = ticketMessage;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public TicketState getStatus() {
        return this.status;
    }

    public void setStatus(TicketState status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

