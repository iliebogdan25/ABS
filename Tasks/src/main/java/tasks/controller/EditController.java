package tasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tasks.model.Task;
import tasks.services.TaskIO;

public class EditController extends NewEditController {
    @FXML
    public void saveChanges() {
        try {
            Task collectedFieldsTask = makeTask();
            for (int i = 0; i < tasksList.size(); i++) {
                if (currentTask.equals(tasksList.get(i))) {
                    tasksList.set(i, collectedFieldsTask);
                }
            }
            currentTask = null;

            TaskIO.rewriteFile(tasksList);
            Controller.editNewStage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
}
