package com.ipl.ipldashboard.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchInput {

    private String id;
    private String season;
    private String city;
    private String date;
    private String team1;
    private String team2;
    private String toss_winner;
    private String toss_decision;
    private String result;
    private String dl_applied;
    private String winner;
    private String win_by_runs;
    private String win_by_wickets;
    private String player_of_match;
    private String venue;
    private String umpire1;
    private String umpire2;
    private String umpire3;

}
