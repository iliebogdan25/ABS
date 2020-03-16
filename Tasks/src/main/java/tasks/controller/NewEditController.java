package tasks.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TasksService;

import java.time.LocalDate;
import java.util.Date;


public abstract class NewEditController {

    private enum WindowType {
        NEW, EDIT;
    }
    private static Button clickedButton;

    private static final Logger log = Logger.getLogger(NewEditController.class.getName());

    public static void setClickedButton(Button clickedButton) {
        NewEditController.clickedButton = clickedButton;
    }

    public static void setCurrentStage(Stage currentStage) {
        NewEditController.currentStage = currentStage;
    }

    private static Stage currentStage;

    protected Task currentTask;
    protected ObservableList<Task> tasksList;
    private TasksService service;
    private DateService dateService;


    @FXML
    private TextField fieldTitle;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private TextField txtFieldTimeStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private TextField txtFieldTimeEnd;
    @FXML
    private TextField fieldInterval;
    @FXML
    private CheckBox checkBoxActive;
    @FXML
    private CheckBox checkBoxRepeated;

    private static final String DEFAULT_START_TIME = "8:00";
    private static final String DEFAULT_END_TIME = "10:00";
    private static final String DEFAULT_INTERVAL_TIME = "0:30";

    public void setTasksList(ObservableList<Task> tasksList) {
        this.tasksList = tasksList;
    }

    public void setService(TasksService service) {
        this.service = service;
        this.dateService = new DateService(service);
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
        switch (clickedButton.getId()) {
            case "btnNew":
                initNewWindow("New Task");
                break;
            case "btnEdit":
                initEditWindow("Edit Task");
                break;
        }
    }

    public WindowType getWindowType() {
        switch (clickedButton.getId()) {
            case "btnNew":
                return WindowType.NEW;
            case "btnEdit":
                return WindowType.EDIT;
        }
        return null;
    }

    @FXML
    public void initialize() {
        log.info("new/edit window initializing");
        datePickerEnd.setValue(LocalDate.now());
        fieldInterval.setText(DEFAULT_INTERVAL_TIME);
//        switch (clickedButton.getId()){
//            case  "btnNew" : initNewWindow("New Task");
//                break;
//            case "btnEdit" : initEditWindow("Edit Task");
//                break;
//        }

    }

    private void initNewWindow(String title) {
        currentStage.setTitle(title);
        datePickerStart.setValue(LocalDate.now());
        txtFieldTimeStart.setText(DEFAULT_START_TIME);
        txtFieldTimeEnd.setText(DEFAULT_END_TIME);
    }

    private void initEditWindow(String title) {
        currentStage.setTitle(title);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(dateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        txtFieldTimeStart.setText(dateService.getTimeOfTheDayFromDate(currentTask.getStartTime()));

        if (currentTask.isRepeated()) {
            checkBoxRepeated.setSelected(true);
            hideRepeatedTaskModule(false);
            datePickerEnd.setValue(dateService.getLocalDateValueFromDate(currentTask.getEndTime()));
            fieldInterval.setText(service.getIntervalInHours(currentTask));
            txtFieldTimeEnd.setText(dateService.getTimeOfTheDayFromDate(currentTask.getEndTime()));
        }
        if (currentTask.isActive()) {
            checkBoxActive.setSelected(true);
        }
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

    private void hideRepeatedTaskModule(boolean toShow) {
        fieldInterval.setDisable(toShow);

        txtFieldTimeEnd.setText(DEFAULT_END_TIME);
    }

    @FXML
    public abstract void saveChanges();

    @FXML
    public void closeDialogWindow() {
        Controller.editNewStage.close();
    }


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
            result = new Task(newTitle, newStartDate, newStartDate);
        }
        boolean isActive = checkBoxActive.isSelected();
        result.setActive(isActive);
        System.out.println(result);
        return result;
    }
}
