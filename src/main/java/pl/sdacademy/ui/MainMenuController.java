package pl.sdacademy.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.entity.Team;
import pl.sdacademy.ui.roundstats.RoundStatsController;

import java.io.IOException;


public class MainMenuController {
    @FXML
    private Button startButton;
    @FXML
    private Button editorButton;
    @FXML
    private ChoiceBox<Team> teamChoiceBox;

    private Stage stage;

    public void initialize(Stage stage) {
        this.stage = stage;

        teamChoiceBox.setConverter(new StringConverter<Team>() {
            @Override
            public String toString(Team team) {
                return team.getName();
            }

            @Override
            public Team fromString(String string) {
                return null;
            }
        });

        startButton.setOnAction(event -> openGameWindow());

        editorButton.setOnAction(event -> openEditorWindow());

        fillTeamChoiceBox();
    }

    private void fillTeamChoiceBox() {
        ObservableList<Team> teams = FXCollections.observableArrayList(DaoManager.TEAM_DAO.getAll());
        teamChoiceBox.setItems(teams);

        if(teams.size() > 0) {
            teamChoiceBox.getSelectionModel().select(1);
        }
    }

    private void openEditorWindow() {
        Stage editorStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TeamsEdit.fxml"));
        HBox root = null;
        try {
            root = fxmlLoader.load();
            TeamsEditController controller = fxmlLoader.getController();
            controller.initialize(stage);
            editorStage.setTitle("Football Manager");
            editorStage.setScene(new Scene(root, 602, 500));
            editorStage.show();

            editorStage.setOnCloseRequest(event -> fillTeamChoiceBox());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd FXMLLoadera");
        }
    }

    private void openGameWindow() {
        Stage editorStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RoundStats.fxml"));
        Team selectedTeam = teamChoiceBox.getSelectionModel().getSelectedItem();
        if(selectedTeam == null) {
            return;
        }
        int selectedTeamId = selectedTeam.getId();
        VBox root = null;
        try {
            root = fxmlLoader.load();
            RoundStatsController controller = fxmlLoader.getController();
            controller.initialize(selectedTeamId);
            editorStage.setTitle("Football Manager");
            editorStage.setScene(new Scene(root, 602, 500));
            editorStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd FXMLLoadera");
        }
    }

}
