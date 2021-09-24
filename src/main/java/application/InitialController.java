package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class InitialController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    PlayTimeCalculator calculator;
    @FXML private Button initialButton;
    @FXML private CheckBox initCheckBox;
    @FXML private Label initLabel;

    public void initialize(){
        initialButton.setDisable(true);
        initCheckBox.setOpacity(0);
        initCheckBox.setDisable(true);
    }

    public void switchToOverviewScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OverviewScene.fxml"));
        root = loader.load();
        OverviewController overviewController = loader.getController();
        overviewController.initialize(calculator);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void enableNextScene(PlayTimeCalculator calculator){
        this.calculator = calculator;
        initialButton.setDisable(false);
        initCheckBox.setOpacity(1);
    }

    public void changeLabel() {
        initLabel.setText("Thank you for the patience\nPlease click 'Next'");
    }
}
