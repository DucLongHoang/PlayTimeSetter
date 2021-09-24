package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) throws InterruptedException {
        BrowserManipulator bm = new BrowserManipulator();
        bm.setPlayTime(60, 45);
        //launch(args);
    }

    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InitialScene.fxml"));
            Parent root = loader.load();
            InitialController initCtrl = loader.getController();

            Scene scene = new Scene(root);
            stage.setTitle("Play time setter");
            stage.setScene(scene);
            stage.show();

            Thread thread = new BrowserThread(initCtrl);
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
