package tasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tasks.model.Task;
import tasks.services.TaskIO;
import tasks.utils.ConstantUtils;

import java.time.LocalDate;

public class NewController extends NewEditController{
    public NewController(final String windowTitlte) {
        super(windowTitlte);
    }

    @Override
    public void initWindow() {
        currentStage.setTitle(windowTitle);
        datePickerStart.setValue(LocalDate.now());
        txtFieldTimeStart.setText(ConstantUtils.DEFAULT_START_TIME);
        txtFieldTimeEnd.setText(ConstantUtils.DEFAULT_END_TIME);
    }

    @FXML
    public void saveChanges() {
        try {
            Task collectedFieldsTask = makeTask();
            tasksList.add(collectedFieldsTask);
            TaskIO.rewriteFile(tasksList);
            this.currentStage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
}
