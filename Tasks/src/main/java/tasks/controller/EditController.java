package tasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TaskIO;

public class EditController extends NewEditController {
    private final Task currentTask;

    public EditController(final String windowTitle, final Task currentTask) {
        super(windowTitle);
        this.currentTask = currentTask;
    }

    @FXML
    public void saveChanges() {
        try {
            Task collectedFieldsTask = makeTask();
            for (int i = 0; i < tasksList.size(); i++) {
                if (currentTask.equals(tasksList.get(i))) {
                    tasksList.set(i, collectedFieldsTask);
                }
            }

            this.currentStage.close();
            TaskIO.rewriteFile(tasksList);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public void initWindow() {
        currentStage.setTitle(windowTitle);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(DateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        txtFieldTimeStart.setText(dateService.getTimeOfTheDayFromDate(currentTask.getStartTime()));
        datePickerEnd.setValue(DateService.getLocalDateValueFromDate(currentTask.getEndTime()));
        fieldInterval.setText(service.getIntervalInHours(currentTask));
        txtFieldTimeEnd.setText(dateService.getTimeOfTheDayFromDate(currentTask.getEndTime()));

        if (currentTask.isRepeated()) {
            checkBoxRepeated.setSelected(true);
            hideRepeatedTaskModule(false);
        }
        if (currentTask.isActive()) {
            checkBoxActive.setSelected(true);
        }
    }
}
