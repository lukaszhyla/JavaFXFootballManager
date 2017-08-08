package pl.sdacademy.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.dao.TeamDao;
import pl.sdacademy.entity.Team;
import pl.sdacademy.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class TeamsEditController {
    @FXML
    private TextArea infoTextArea;
    @FXML
    private Button resetFormButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker foundationDatePicker;
    @FXML
    private ListView<Team> teamsListView;
    @FXML
    private Label formTitleLabel;
    @FXML
    private Button playersEditButton;

    private Stage stage;

    private TeamDao teamDao;

    public void initialize(Stage stage) {
        this.stage = stage;
        teamDao = DaoManager.TEAM_DAO;
        loadTeams();

        infoTextArea.setPromptText("Wpisz info");

        Utils.setControlVisibility(playersEditButton, false);
        Utils.setControlVisibility(deleteButton, false);

        teamsListView.setCellFactory(param -> new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    String teamCellText = Utils.ellipsize(team.getName(), 15);
                    setText(teamCellText);
                }
            }
        });

        resetFormButton.setOnAction(event -> {
            resetForm();
        });

        teamsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                fillForm(newValue);
            } else {
                resetForm();
            }
        });
        saveButton.setOnAction(event -> {
            Team selectedTeam = teamsListView.getSelectionModel().getSelectedItem();
            if(selectedTeam == null) {
                saveNewTeam();
            } else {
                updateSelectedTeam(selectedTeam);
            }
        });

        deleteButton.setOnAction(event -> {
            Team selectedTeam = teamsListView.getSelectionModel().getSelectedItem();
            teamDao.delete(selectedTeam.getId());
            teamsListView.getItems().remove(teamsListView.getSelectionModel().getSelectedIndex());
        });

        playersEditButton.setOnAction(event -> {
            Stage playersEditStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayersEdit.fxml"));
            try {
                Parent playersEditRoot = fxmlLoader.load();
                PlayersEditController controller = fxmlLoader.getController();
                Team selectedTeam = teamsListView.getSelectionModel().getSelectedItem();
                controller.initialize(selectedTeam.getId());
                playersEditStage.setScene(new Scene(playersEditRoot, 500, 500));
                // Opisuję, że okno będzie modalem, którego ownerem będzie okno stage
                playersEditStage.initModality(Modality.WINDOW_MODAL);
                playersEditStage.initOwner(stage);
                playersEditStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadTeams() {
        List<Team> teams = teamDao.getAll();
        teamsListView.setItems(FXCollections.observableArrayList(teams));
    }

    private void fillForm(Team team) {
        nameTextField.setText(team.getName());
        infoTextArea.setText(team.getInfo());
        foundationDatePicker.setValue(team.getFoundationDate());
        Utils.setControlVisibility(deleteButton, true);
        Utils.setControlVisibility(playersEditButton, true);
        formTitleLabel.setText("Edycja drużyny");
    }

    private void resetForm() {
        nameTextField.setText("");
        infoTextArea.setText("");
        foundationDatePicker.setValue(null);
        formTitleLabel.setText("Dodawanie drużyny");
        Utils.setControlVisibility(deleteButton, false);
        teamsListView.getSelectionModel().clearSelection();
    }

    private void saveNewTeam() {
        String name = nameTextField.getText();
        String info = infoTextArea.getText();
        LocalDate foundationDate = foundationDatePicker.getValue();
        Team newTeam = new Team(name, foundationDate, info);
        teamDao.add(newTeam);
        teamsListView.getItems().add(newTeam);
        Platform.runLater(() -> {
            teamsListView.getSelectionModel().selectLast();
            fillForm(newTeam);
        });
    }

    private void updateSelectedTeam(Team selectedTeam) {
        selectedTeam.setFoundationDate(foundationDatePicker.getValue());
        selectedTeam.setName(nameTextField.getText());
        selectedTeam.setInfo(infoTextArea.getText());
        teamDao.update(selectedTeam);
        teamsListView.getItems().set(teamsListView.getSelectionModel().getSelectedIndex(), selectedTeam);
        fillForm(selectedTeam);
    }
}
