package tasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;


public class TaskInfoController {

    private static final Logger log = Logger.getLogger(TaskInfoController.class.getName());
    private final Task task;
    private final Stage stage;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelStart;
    @FXML
    private Label labelEnd;
    @FXML
    private Label labelInterval;
    @FXML
    private Label labelIsActive;

    public TaskInfoController(final Task task, final Stage stage) {
        this.task = task;
        this.stage = stage;
    }


    @FXML
    public void initialize(){
        log.info("task info window initializing");

        labelTitle.setText("Title: " + task.getTitle());
        labelStart.setText("Start time: " + task.getFormattedDateStart());
        labelEnd.setText("End time: " + task.getFormattedDateEnd());
        labelInterval.setText("Interval: " + task.getFormattedRepeated());
        labelIsActive.setText("Is active: " + (task.isActive() ? "Yes" : "No"));
    }
    @FXML
    public void closeWindow(){
        this.stage.close();
    }

}
