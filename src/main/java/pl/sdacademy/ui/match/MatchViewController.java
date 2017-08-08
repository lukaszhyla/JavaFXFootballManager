package pl.sdacademy.ui.match;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.entity.Match;
import pl.sdacademy.entity.Team;

/**
 * Created by D on 11.05.2017.
 */
public class MatchViewController {
    @FXML
    private Label team1NameLabel;
    @FXML
    private Label team2NameLabel;
    @FXML
    private Label team1GoalsLabel;
    @FXML
    private Label team2GoalsLabel;
    @FXML
    private Label clockLabel;

    public void initialize(Match playersMatch, Stage matchStage) {
        Team team1 = DaoManager.TEAM_DAO.get(playersMatch.getTeam1Id());
        Team team2 = DaoManager.TEAM_DAO.get(playersMatch.getTeam2Id());

        team1NameLabel.setText(team1.getName());
        team2NameLabel.setText(team2.getName());

        new Thread(new MatchRunnable(playersMatch) {
            @Override
            void delay(int i) {
                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    clockLabel.setText(Integer.toString(i));
                });
            }
            @Override
            void onTeam1GoalScored(int team1Goals) {
                Platform.runLater(() -> {
                    team1GoalsLabel.setText(Integer.toString(team1Goals));
                });
            }
            @Override
            void onTeam2GoalScored(int team2Goals) {
                Platform.runLater(() -> {
                    team2GoalsLabel.setText(Integer.toString(team2Goals));
                });
            }
            @Override
            void onEnd() {
                Platform.runLater(() -> {
                    matchStage.close();
                });
            }
        }).start();
    }

    public Label getTeam1GoalsLabel() {
        return team1GoalsLabel;
    }

    public Label getTeam2GoalsLabel() {
        return team2GoalsLabel;
    }

    public Label getClockLabel() {
        return clockLabel;
    }
}
