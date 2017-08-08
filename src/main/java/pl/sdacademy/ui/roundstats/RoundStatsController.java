package pl.sdacademy.ui.roundstats;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.entity.Match;
import pl.sdacademy.entity.Team;
import pl.sdacademy.ui.match.MatchRunnable;
import pl.sdacademy.ui.match.MatchViewController;
import pl.sdacademy.util.DataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoundStatsController {
    @FXML
    private TableColumn<TeamRoundStatsTableRow, Integer> ordinalColumn;
    @FXML
    private TableColumn<TeamRoundStatsTableRow, String> nameColumn;
    @FXML
    private TableColumn<TeamRoundStatsTableRow, Integer> pointsColumn;
    @FXML
    private TableColumn<TeamRoundStatsTableRow, Integer> goalsScoredColumn;
    @FXML
    private TableColumn<TeamRoundStatsTableRow, Integer> goalsConcededColumn;
    @FXML
    private TableView<TeamRoundStatsTableRow> tableView;
    @FXML
    private Label titleLabel;
    @FXML
    private Slider roundNoSlider;
    @FXML
    private Button playNextRoundButton;

    private int playerTeamId;

    public void initialize(int playerTeamId) {
        this.playerTeamId = playerTeamId;
        // Jeśli chcemy dopisać "customową" wartość wyświetlaną w kolumnie, możemy użyć sposobu:
        ordinalColumn.setCellValueFactory(column -> {
            TeamRoundStatsTableRow row = column.getValue();
            return new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(row) + 1);
        });

        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        goalsScoredColumn.setCellValueFactory(new PropertyValueFactory<>("goalsScored"));
        goalsConcededColumn.setCellValueFactory(new PropertyValueFactory<>("goalsConceded"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        tableView.setRowFactory(tv -> new TableRow<TeamRoundStatsTableRow>() {
            @Override
            public void updateItem(TeamRoundStatsTableRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.getTeamId() == playerTeamId) {
                    setStyle("-fx-background-color: #aaaaaa;");
                } else {
                    setStyle("");
                }
            }
        });

        roundNoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            showDataForRoundNo(newValue.intValue());
        });
        int lastRoundNo = DaoManager.MATCH_DAO.getLastRoundNo();
        roundNoSlider.setMax(lastRoundNo);
        roundNoSlider.setValue(lastRoundNo);
        showDataForRoundNo(lastRoundNo);
        playNextRoundButton.setOnAction(event -> {
            startNextRound();
        });
    }

    private void startNextRound() {
        int nextRoundNo = DaoManager.MATCH_DAO.getLastRoundNo() + 1;
        List<Match> roundMatches = DataGenerator.getRoundMatches(nextRoundNo);
        if (!roundMatches.isEmpty()) {
            Optional<Match> optionalPlayersMatch = roundMatches.stream()
                    .filter(m -> m.getTeam1Id() == playerTeamId || m.getTeam2Id() == playerTeamId)
                    .findFirst();
            Match playersMatch = optionalPlayersMatch.orElse(null);


            if (optionalPlayersMatch.isPresent()) {
                Stage matchStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../MatchView.fxml"));
                HBox root = null;
                try {
                    root = fxmlLoader.load();
                    MatchViewController controller = fxmlLoader.getController();
                    controller.initialize(optionalPlayersMatch.get(), matchStage);
                    matchStage.setTitle("Football Manager");
                    matchStage.setScene(new Scene(root, 602, 500));
                    DaoManager.MATCH_DAO.add(playersMatch);
                    matchStage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            roundMatches
                    .stream()
                    .filter(m -> !m.equals(playersMatch))
                    .forEach(m -> {
                new MatchRunnable(m).run();
                DaoManager.MATCH_DAO.add(m);
            });
            roundNoSlider.setMax(nextRoundNo);
            roundNoSlider.setValue(nextRoundNo);
            showDataForRoundNo(nextRoundNo);
        }
    }
    private void showDataForRoundNo(int roundNo) {
        titleLabel.setText("Tabela dla kolejki nr " + roundNo);

        List<Team> teams = DaoManager.TEAM_DAO.getAll();
        List<TeamRoundStatsTableRow> tableRows = new ArrayList<>();
        for (Team team : teams) {
            List<Match> teamMatches = DaoManager.MATCH_DAO.getMatchesForTeamTillRound(team.getId(), roundNo);
            tableRows.add(new TeamRoundStatsTableRow(team, teamMatches));
        }
        // sortujemy wiersze, najpierw wedle roznicy punktow, nastepnie wedlug roznicy goli (strzelonych i straconych)
        tableRows = tableRows.stream()
                .sorted((tr1, tr2) -> {
                    int pointsDifference = tr2.getPoints() - tr1.getPoints();
                    if (pointsDifference != 0) {
                        return pointsDifference;
                    } else {
                        return (tr2.getGoalsScored() - tr2.getGoalsConceded())
                                - (tr1.getGoalsScored() - tr1.getGoalsConceded());
                    }
                })
                .collect(Collectors.toList());


        tableView.setItems(FXCollections.observableArrayList(tableRows));
    }
}
