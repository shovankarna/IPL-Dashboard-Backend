package com.ipl.ipldashboard.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Match {

    @Id
    private long id;
    private String season;
    private String city;
    private LocalDate date;
    private String team1;
    private String team2;
    private String tossWinner;
    private String tossDecision;
    private String result;
    private boolean dlApplied;
    private String matchWinner;
    private Integer winByRuns;
    private Integer winByWickets;
    private String playerOfMatch;
    private String venue;
    private String umpire1;
    private String umpire2;
    private String umpire3;
} 
