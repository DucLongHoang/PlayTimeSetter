package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class OverviewController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private PlayTimeCalculator calculator;
    @FXML private TableView<MarkTime> markTimeTable;
    @FXML private TableColumn<MarkTime, String> markColumn, timeColumn;
    @FXML private Label playTimeLabel, dateLabel, finalTime;
    @FXML private Slider saturdaySlider, sundaySlider;
    private int time;
    private int saturdayTime, sundayTime;

    public void switchToMarksScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MarksScene.fxml"));
        root = loader.load();
        MarksController marksController = loader.getController();
        marksController.initialize(calculator);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(PlayTimeCalculator calculator){
        this.calculator = calculator;
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        markTimeTable.setItems(getMarkTime());

        playTimeLabel.setText((int) calculator.getPlayTime() + " min");
        dateLabel.setText(calculator.getWeek());
        this.time = calculator.getFinalPlayTime();
        setFinalTime(0);
        //finalTime.setText(time + " min");

    }

    public void setFinalTime(int offset) {
        this.finalTime.setText((this.time - offset) + " min");
    }

    public ObservableList<MarkTime> getMarkTime(){
        ObservableList<MarkTime> result = FXCollections.observableArrayList();
        result.add(new MarkTime(1, 30));
        result.add(new MarkTime(2, 0));
        result.add(new MarkTime(3, -60));
        result.add(new MarkTime(4, -120));
        result.add(new MarkTime(5, -180));

        return result;
    }

    public void setTime() throws InterruptedException {
        BrowserManipulator bm = new BrowserManipulator();
        bm.setPlayTime(this.saturdayTime, this.sundayTime);
    }

    public class MarkTime {
        private int mark;
        private String time;

        public MarkTime(int mark, int time){
            this.mark = mark;
            this.time = time + " min";
        }
        public int getMark() {
            return mark;
        }
        public String getTime() {
            return time;
        }
        public void setTime(int time) {
            this.time = time + " min";
        }
        public void setMark(int mark) {
            this.mark = mark;
        }
    }

    public void setPlayTime() {
        double value1 = saturdaySlider.getValue();
        double value2 = sundaySlider.getValue();
        System.out.println("Sat: " + value1);
        System.out.println("Sun: " + value2);
        setFinalTime((int) (value1 + value2));
        this.saturdayTime = (int) value1;
        this.sundayTime = (int) value2;
    }
}
