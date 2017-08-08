package pl.sdacademy.entity;


public class Match extends Entity {
    private int team1Id;
    private int team2Id;
    private int team1Goals;
    private int team2Goals;
    private int roundNo;

    public Match(int team1Id, int team2Id, int team1Goals, int team2Goals, int roundNo) {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.team1Goals = team1Goals;
        this.team2Goals = team2Goals;
        this.roundNo = roundNo;
    }

    public Match(Integer id, int team1Id, int team2Id, int team1Goals, int team2Goals, int roundNo) {
        super(id);
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.team1Goals = team1Goals;
        this.team2Goals = team2Goals;
        this.roundNo = roundNo;
    }

    public Match(Match match) {
        super(match.getId());
        this.team1Goals = match.team1Goals;
        this.team2Goals = match.team2Goals;
        this.team1Id = match.team1Id;
        this.team2Id = match.team2Id;
        this.roundNo = match.roundNo;
    }

    public int getPointsForTeam(int teamId) {
        int goalsConceded = getGoalsConcededForTeam(teamId);
        int goalsScored = getGoalsScoredForTeam(teamId);
        if(goalsScored - goalsConceded > 0) {
            return 3;
        } else if(goalsConceded == goalsScored) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getGoalsConcededForTeam(int teamId) {
        if(team1Id == teamId) {
            return team2Goals;
        } else {
            return team1Goals;
        }
    }

    public int getGoalsScoredForTeam(int teamId) {
        if(team1Id == teamId) {
            return team1Goals;
        } else {
            return team2Goals;
        }
    }

    public int getTeam1Id() {
        return team1Id;
    }

    public int getTeam2Id() {
        return team2Id;
    }

    public int getTeam1Goals() {
        return team1Goals;
    }

    public int getTeam2Goals() {
        return team2Goals;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public void setTeam1Id(int team1Id) {
        this.team1Id = team1Id;
    }

    public void setTeam2Id(int team2Id) {
        this.team2Id = team2Id;
    }

    public void setTeam1Goals(int team1Goals) {
        this.team1Goals = team1Goals;
    }

    public void setTeam2Goals(int team2Goals) {
        this.team2Goals = team2Goals;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }
}
