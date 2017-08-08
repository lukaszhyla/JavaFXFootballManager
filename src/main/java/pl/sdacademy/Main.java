package pl.sdacademy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.dao.PlayerDao;
import pl.sdacademy.entity.Player;
import pl.sdacademy.ui.MainMenuController;
import pl.sdacademy.ui.roundstats.RoundStatsController;
import pl.sdacademy.util.DataGenerator;

import java.util.List;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/MainMenu.fxml"));
        VBox root = fxmlLoader.load();
        MainMenuController controller = fxmlLoader.getController();
        controller.initialize(primaryStage);
        primaryStage.setTitle("Football Manager");
        primaryStage.setScene(new Scene(root, 602, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        DaoManager.MATCH_DAO.deleteAll();
        launch(args);
    }
}
