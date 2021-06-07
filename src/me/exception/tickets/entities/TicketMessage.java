

package me.exception.tickets.entities;

public class TicketMessage
{
    private int ticketId;
    private String message;
    private String sender;
    
    public TicketMessage(final int ticketId, final String message, final String sender) {
        this.ticketId = ticketId;
        this.message = message;
        this.sender = sender;
    }
    
    public int getTicketId() {
        return this.ticketId;
    }
    
    public void setTicketId(final int ticketId) {
        this.ticketId = ticketId;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getSender() {
        return this.sender;
    }
    
    public void setSender(final String sender) {
        this.sender = sender;
    }
}
