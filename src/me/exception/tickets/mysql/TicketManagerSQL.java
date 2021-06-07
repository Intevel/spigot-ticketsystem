package me.exception.tickets.mysql;

import com.google.common.collect.Lists;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import me.exception.tickets.SystemMain;
import me.exception.tickets.entities.Ticket;
import me.exception.tickets.entities.TicketState;


public class TicketManagerSQL {

    private AsyncMySQL asyncMySQL = SystemMain.getInstance().getAsyncMySQL();


    public void getTicketById(int id, Consumer<Ticket> ticketConsumer) {
        try {
            PreparedStatement statement = this.asyncMySQL.prepare("SELECT * FROM tickets WHERE id = ?;");
            statement.setInt(1, id);
            this.asyncMySQL.query(statement, resultSet -> {
                try {
                    if (resultSet.next()) {
                        Ticket ticket = new Ticket(id, resultSet.getString("name"), resultSet.getString("sender"), resultSet.getString("message"), resultSet.getString("category"), TicketState.valueOf(resultSet.getString("state".toUpperCase())));


                        ticketConsumer.accept(ticket);


                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ticketConsumer.accept(null);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTicket(String name, String sender, String msg, String kategorie, TicketState status) {
        getTickets(tickets -> {
            try {
                PreparedStatement statement = this.asyncMySQL.prepare("INSERT INTO tickets VALUES (?,?,?,?,?,?)");
                statement.setInt(1, getFreeId(tickets));
                statement.setString(2, name);
                statement.setString(3, sender);
                statement.setString(4, kategorie);
                statement.setString(5, msg);
                statement.setString(6, status.toString());
                this.asyncMySQL.update(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private int getFreeId(List<Ticket> tickets) {
        int id = (new Random()).nextInt(999999);
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return getFreeId(tickets);
            }
        }
        return id;
    }

    public void getTickets(Consumer<List<Ticket>> listConsumer) {
        List<Ticket> tickets = Lists.newArrayList();
        this.asyncMySQL.query("SELECT * FROM tickets", resultSet -> {
            try {
                while (resultSet.next()) {
                    Ticket ticket = new Ticket(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("sender"), resultSet.getString("message"), resultSet.getString("category"), TicketState.valueOf(resultSet.getString("state".toUpperCase())));


                    tickets.add(ticket);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            listConsumer.accept(tickets);
        });
    }


    public void removeTicket(int id) {
        this.asyncMySQL.update("DELETE FROM tickets WHERE id = '" + id + "';");
    }

    public void changeState(int id, TicketState ticketState) {
        this.asyncMySQL.update("UPDATE tickets SET state = '" + ticketState.toString() + "' WHERE id = '" + id + "'");
    }
}

    