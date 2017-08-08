package pl.sdacademy.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.dao.PlayerDao;
import pl.sdacademy.entity.Player;
import pl.sdacademy.util.Utils;

import java.time.LocalDate;
import java.util.List;


public class PlayersEditController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private ListView<Player> playersListView;
    @FXML
    private Label formTitleLabel;
    @FXML
    private Slider shootingSlider;
    @FXML
    private Slider staminaSlider;
    @FXML
    private Slider speedSlider;
    @FXML
    private Button resetFormButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label staminaLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private Label shootingLabel;

    private PlayerDao playerDao;

    private int teamId;

    public void initialize(int teamId) {
        this.teamId = teamId;
        playerDao = DaoManager.PLAYER_DAO;
        loadPlayers();
        Utils.setControlVisibility(deleteButton, false);

        playersListView.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                } else {
                    String playerCellText = Utils.ellipsize(player.getName() + " " + player.getSurname(), 15);
                    setText(playerCellText);
                }
            }
        });

        resetFormButton.setOnAction(event -> {
            resetForm();
        });

        playersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                fillForm(newValue);
            } else {
                resetForm();
            }
        });
        saveButton.setOnAction(event -> {
            Player selectedPlayer = playersListView.getSelectionModel().getSelectedItem();
            if(selectedPlayer == null) {
                saveNewPlayer();
            } else {
                updateSelectedPlayer(selectedPlayer);
            }
        });

        deleteButton.setOnAction(event -> {
            Player selectedPlayer = playersListView.getSelectionModel().getSelectedItem();
            playerDao.delete(selectedPlayer.getId());
            playersListView.getItems().remove(playersListView.getSelectionModel().getSelectedIndex());
        });

        bindSliderToLabel(staminaSlider, staminaLabel);
        bindSliderToLabel(shootingSlider, shootingLabel);
        bindSliderToLabel(speedSlider, speedLabel);
    }

    private void bindSliderToLabel(Slider slider, Label label) {
        label.setText(Integer.toString((int) slider.getValue()));
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(Integer.toString((int) slider.getValue()));
        });
    }

    private void loadPlayers() {
        List<Player> players = playerDao.getByTeamId(teamId);
        playersListView.setItems(FXCollections.observableArrayList(players));
    }

    private void fillForm(Player player) {
        nameTextField.setText(player.getName());
        surnameTextField.setText(player.getSurname());
        dobDatePicker.setValue(player.getDateOfBirth());
        staminaSlider.setValue(player.getStamina());
        speedSlider.setValue(player.getSpeed());
        shootingSlider.setValue(player.getShooting());
        Utils.setControlVisibility(deleteButton, true);
        formTitleLabel.setText("Edycja piłkarza");
    }

    private void resetForm() {
        nameTextField.setText("");
        surnameTextField.setText("");
        speedSlider.setValue(1);
        staminaSlider.setValue(1);
        shootingSlider.setValue(1);
        dobDatePicker.setValue(null);
        formTitleLabel.setText("Dodawanie piłkarza");
        Utils.setControlVisibility(deleteButton, false);
        playersListView.getSelectionModel().clearSelection();
    }

    private void saveNewPlayer() {
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        LocalDate dateOfBirth = dobDatePicker.getValue();
        int speed = (int) speedSlider.getValue();
        int stamina = (int) staminaSlider.getValue();
        int shooting = (int) shootingSlider.getValue();

        Player newPlayer = new Player(teamId,
                name,
                surname,
                dateOfBirth,
                speed,
                shooting,
                stamina);
        playerDao.add(newPlayer);
        playersListView.getItems().add(newPlayer);
        Platform.runLater(() -> {
            playersListView.getSelectionModel().selectLast();
            fillForm(newPlayer);
        });
    }

    private void updateSelectedPlayer(Player selectedPlayer) {
        selectedPlayer.setDateOfBirth(dobDatePicker.getValue());
        selectedPlayer.setName(nameTextField.getText());
        selectedPlayer.setSurname(surnameTextField.getText());
        selectedPlayer.setSpeed((int) speedSlider.getValue());
        selectedPlayer.setShooting((int) shootingSlider.getValue());
        selectedPlayer.setStamina((int) staminaSlider.getValue());
        playerDao.update(selectedPlayer);
        playersListView.getItems().set(playersListView.getSelectionModel().getSelectedIndex(), selectedPlayer);
        fillForm(selectedPlayer);
    }
}
