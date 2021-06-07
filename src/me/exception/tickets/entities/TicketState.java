

package me.exception.tickets.entities;

public enum TicketState
{
    PENDING("§cAusstehend"), 
    PROCESSING("§eBearbeitung"), 
    FINISH("§aGeschlossen");
    
    private String name;
    
    private TicketState(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
