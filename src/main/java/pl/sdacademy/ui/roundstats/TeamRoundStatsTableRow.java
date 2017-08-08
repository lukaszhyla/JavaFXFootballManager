package pl.sdacademy.ui.roundstats;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import pl.sdacademy.entity.Match;
import pl.sdacademy.entity.Team;

import java.util.List;

public class TeamRoundStatsTableRow {
    private SimpleIntegerProperty teamId;
    private SimpleStringProperty teamName;
    private SimpleIntegerProperty points;
    private SimpleIntegerProperty goalsConceded;
    private SimpleIntegerProperty goalsScored;

    public TeamRoundStatsTableRow(Team team, List<Match> matches) {
        int teamId = team.getId();
        this.teamId = new SimpleIntegerProperty(teamId);
        this.teamName = new SimpleStringProperty(team.getName());
        int points = matches.stream()
                // mapujemy do IntStream, który oferuje nam metodę sum
                .mapToInt(m -> m.getPointsForTeam(teamId))
                .sum();
        this.points = new SimpleIntegerProperty(points);
        int goalsConceded = matches.stream()
                .mapToInt(m -> m.getGoalsConcededForTeam(teamId))
                .sum();
        this.goalsConceded = new SimpleIntegerProperty(goalsConceded);
        int goalsScored = matches.stream()
                .mapToInt(m -> m.getGoalsScoredForTeam(teamId))
                .sum();
        this.goalsScored = new SimpleIntegerProperty(goalsScored);
    }

    public int getTeamId() {
        return teamId.get();
    }

    public SimpleIntegerProperty teamIdProperty() {
        return teamId;
    }

    public String getTeamName() {
        return teamName.get();
    }

    public SimpleStringProperty teamNameProperty() {
        return teamName;
    }

    public int getPoints() {
        return points.get();
    }

    public SimpleIntegerProperty pointsProperty() {
        return points;
    }

    public int getGoalsConceded() {
        return goalsConceded.get();
    }

    public SimpleIntegerProperty goalsConcededProperty() {
        return goalsConceded;
    }

    public int getGoalsScored() {
        return goalsScored.get();
    }

    public SimpleIntegerProperty goalsScoredProperty() {
        return goalsScored;
    }
}
