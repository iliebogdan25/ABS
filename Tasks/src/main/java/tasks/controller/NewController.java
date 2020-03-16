package tasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tasks.model.Task;
import tasks.services.TaskIO;

public class NewController extends NewEditController{
    @FXML
    public void saveChanges() {
        try {
            Task collectedFieldsTask = makeTask();
            tasksList.add(collectedFieldsTask);
            TaskIO.rewriteFile(tasksList);
            Controller.editNewStage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
}
