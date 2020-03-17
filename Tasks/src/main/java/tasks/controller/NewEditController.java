package tasks.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TasksService;
import tasks.utils.ConstantUtils;

import java.time.LocalDate;
import java.util.Date;


public abstract class NewEditController {
    private static final Logger log = Logger.getLogger(NewEditController.class.getName());
    protected final String windowTitle;
    protected Stage currentStage;
    protected ObservableList<Task> tasksList;
    protected TasksService service;
    protected DateService dateService;
    @FXML
    protected TextField fieldTitle;
    @FXML
    protected DatePicker datePickerStart;
    @FXML
    protected TextField txtFieldTimeStart;
    @FXML
    protected DatePicker datePickerEnd;
    @FXML
    protected TextField txtFieldTimeEnd;
    @FXML
    protected TextField fieldInterval;
    @FXML
    protected CheckBox checkBoxActive;
    @FXML
    protected CheckBox checkBoxRepeated;

    public NewEditController(final String windowTitle) {
        this.windowTitle = windowTitle;
    }


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
        initWindow();
    }



    public void setTasksList(ObservableList<Task> tasksList) {
        this.tasksList = tasksList;
    }

    public void setService(TasksService service) {
        this.service = service;
        this.dateService = new DateService(service);
    }

    public abstract void initWindow();

    @FXML
    public void initialize() {
        log.info("new/edit window initializing");
        datePickerEnd.setValue(LocalDate.now());
        fieldInterval.setText(ConstantUtils.DEFAULT_INTERVAL_TIME);
    }

    @FXML
    public void switchRepeatedCheckbox(ActionEvent actionEvent) {
        CheckBox source = (CheckBox) actionEvent.getSource();
        if (source.isSelected()) {
            hideRepeatedTaskModule(false);
        } else if (!source.isSelected()) {
            hideRepeatedTaskModule(true);
        }
    }

    protected void hideRepeatedTaskModule(boolean toShow) {
        fieldInterval.setDisable(toShow);

        txtFieldTimeEnd.setText(ConstantUtils.DEFAULT_END_TIME);
    }

    @FXML
    public abstract void saveChanges();



    protected Task makeTask() throws IllegalArgumentException {
        Task result;
        String newTitle = fieldTitle.getText();
        Date startDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerStart.getValue());//ONLY date!!without time
        Date newStartDate = dateService.getDateMergedWithTime(txtFieldTimeStart.getText(), startDateWithNoTime);
        Date endDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerEnd.getValue());
        Date newEndDate = dateService.getDateMergedWithTime(txtFieldTimeEnd.getText(), endDateWithNoTime);
        if (newStartDate.after(newEndDate)) throw new IllegalArgumentException("Start date should be before end");
        if (checkBoxRepeated.isSelected()) {
            int newInterval = service.parseFromStringToSeconds(fieldInterval.getText());
            result = new Task(newTitle, newStartDate, newEndDate, newInterval);
        } else {
            result = new Task(newTitle, newStartDate, newEndDate);
        }
        boolean isActive = checkBoxActive.isSelected();
        result.setActive(isActive);
        System.out.println(result);
        return result;
    }
}
