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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class MarksController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private PlayTimeCalculator calculator;
    @FXML private Button overviewButton;
    @FXML private ToggleButton switchMarksToggle;
    @FXML private ComboBox<String> comboBox;
    @FXML private TableView<MarkEntry> marksTable;
    @FXML private TableView<AverageMark> avgTable;
    @FXML private TableColumn<MarkEntry, LocalDate> dateColumn;
    @FXML private TableColumn<MarkEntry, String> subjectColumn, markColumn,
            weightColumn, avgSubjectCol, avgMarkCol;

    public void initialize(PlayTimeCalculator calculator){
        this.calculator = calculator;
        this.comboBox.getItems().addAll(options());
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        dateColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(dtf.format(date));
                }
            }
        });

        marksTable.setItems(getWeekMarkEntry());
    }

    public ObservableList<MarkEntry> getWeekMarkEntry(){
        ObservableList<MarkEntry> result = FXCollections.observableArrayList();
        result.addAll(calculator.getWeekMarks());

        return result;
    }

    public ObservableList<MarkEntry> getAllMarkEntry(){
        ObservableList<MarkEntry> result = FXCollections.observableArrayList();
        result.addAll(calculator.getAllMarks());

        return result;
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

    public void setSwitchMarksToggle(){
        if(switchMarksToggle.isSelected()){
            switchMarksToggle.setText("Week marks");
            marksTable.setItems(getAllMarkEntry());
        }
        else{
            switchMarksToggle.setText("All marks");
            marksTable.setItems(getWeekMarkEntry());
        }
    }

    public ObservableList<String> options(){
        ObservableList<String> result = FXCollections.observableArrayList();
        String[] subjects = new String[] {
                "Hide", "Anglický jazyk", "Český jazyk a literatura",
                "Dějepis", "Fyzika", "Hudební výchova", "Chemie",
                "Matematika", "Německý jazyk", "Občanská a rodinná výchova",
                "Pracovní činnosti", "Přírodopis", "Tělesná výchova",
                "Výtvarná výchova", "Zeměpis", "Anglická konverzace",
                "Environmentální výchova", "Výpočetní technika", "Zeměpisný seminář"
        };
        result.addAll(subjects);

        return result;
    }

    public void filterSubject(){
        String subject = this.comboBox.getValue();
        if(subject.equals("Hide")){
            avgTable.setDisable(true);
            avgTable.setVisible(false);
        }
        else {
            avgSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
            avgMarkCol.setCellValueFactory(new PropertyValueFactory<>("avg"));
            avgTable.setItems(FXCollections.observableArrayList(
                    new AverageMark(calculator.getAllMarks(), subject)));

            avgTable.setDisable(false);
            avgTable.setVisible(true);
        }
    }

    public class AverageMark{
        private final String subject;
        private double avg;
        private final LinkedList<MarkEntry> list;

        public AverageMark(LinkedList<MarkEntry> list, String subject){
            this.list = list;
            this.subject = subject;
            calculateAvg();
        }

        public void calculateAvg(){
            double mark = 0, weight = 0;
            for(MarkEntry entry: list){
                if(entry.getSubject().equals(subject)){
                    weight += entry.getWeight();
                    mark += (entry.getMark() * entry.getWeight());
                }
            }
            mark /= weight;
            this.avg = (Math.round(mark * 100.0) / 100.0);
        }

        public String getSubject() {
            return subject;
        }
        public double getAvg() {
            return avg;
        }
    }

}
