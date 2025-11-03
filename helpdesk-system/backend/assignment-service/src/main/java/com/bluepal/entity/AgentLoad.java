package com.bluepal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "agent_load")
public class AgentLoad {

    @Id
    private String agentUsername;
    private int assignedTickets;

    public AgentLoad() {
    }

    public AgentLoad(String agentUsername, int assignedTickets) {
        this.agentUsername = agentUsername;
        this.assignedTickets = assignedTickets;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public int getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(int assignedTickets) {
        this.assignedTickets = assignedTickets;
    }
}
