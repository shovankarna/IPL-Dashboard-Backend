package com.ipl.ipldashboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team {
    
    @Id
    private long id;
    private String teamName;
    private long totalMatches;
    private long totalWins;
}
