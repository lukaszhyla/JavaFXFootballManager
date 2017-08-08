package pl.sdacademy.ui.match;

import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.dao.PlayerDao;
import pl.sdacademy.dao.TeamDao;
import pl.sdacademy.entity.Match;
import pl.sdacademy.entity.Player;

import java.util.List;
import java.util.Random;

public class MatchRunnable implements Runnable {
    private Match match;
    private int team1StatsSum;
    private int team2StatsSum;
    List<Player> team1Players;
    List<Player> team2Players;

    public MatchRunnable(Match match) {
        this.match = match;

        TeamDao teamDao;
        PlayerDao playerDao;
        teamDao = DaoManager.TEAM_DAO;
        playerDao = DaoManager.PLAYER_DAO;

        int team1Id = teamDao.get(match.getTeam1Id()).getId();
        int team2Id = teamDao.get(match.getTeam2Id()).getId();

        team1Players = playerDao.getByTeamId(team1Id);
        team2Players = playerDao.getByTeamId(team2Id);

        team1StatsSum = team1Players.stream().
                mapToInt(s -> s.getStamina() + s.getShooting() + s.getSpeed())
                .sum();

        team2StatsSum = team2Players.stream()
                .mapToInt(s -> s.getStamina() + s.getShooting() + s.getStamina())
                .sum();
    }

    @Override
    public void run() {
        Random random = new Random();
        int team1Goals = 1;
        int team2Goals = 1;

        for (int i = 0; i <= 90; i++) {
            delay(i);
            int team1Chance = 10000 + team1StatsSum;
            int team2Chance = 10000 + team2StatsSum;

            if (team1Chance * random.nextInt(100) > 990000) {
                match.setTeam1Goals(team1Goals);
                onTeam1GoalScored(team1Goals);
                team1Goals++;
            }
            if (team2Chance * random.nextInt(100) > 990000) {
                match.setTeam2Goals(team2Goals);
                onTeam2GoalScored(team2Goals);
                team2Goals++;
            }
        }
        onEnd();
    }

    void whoShotGoal() {

    }
    void delay(int i) {}
    void onTeam1GoalScored(int team1Goals){}
    void onTeam2GoalScored(int team2Goals){}
    void onEnd(){}
}
