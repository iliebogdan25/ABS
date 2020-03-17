package tasks.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TaskIO;
import tasks.services.TasksService;
import tasks.utils.ConstantUtils;
import tasks.view.Main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


public class Controller {
    private static final Logger log = Logger.getLogger(Controller.class.getName());
    public ObservableList<Task> tasksList;
    TasksService service;
    DateService dateService;

    public static Stage infoStage;

    public static TableView mainTable;

    @FXML
    public  TableView tasks;
    @FXML
    private TableColumn<Task, String> columnTitle;
    @FXML
    private TableColumn<Task, String> columnTime;
    @FXML
    private TableColumn<Task, String> columnRepeated;
    @FXML
    private Label labelCount;
    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private TextField fieldTimeFrom;
    @FXML
    private DatePicker datePickerTo;
    @FXML
    private TextField fieldTimeTo;

    public void setService(TasksService service){
        this.service=service;
        this.dateService=new DateService(service);
        this.tasksList=service.getObservableList();
        setCountLabel(tasksList.size());
        tasks.setItems(tasksList);
        mainTable = tasks;

        tasksList.addListener((ListChangeListener.Change<? extends Task> c) -> {
            setCountLabel(tasksList.size());
                    tasks.setItems(tasksList);
                }
        );
    }

    @FXML
    public void initialize(){
        log.info("Main controller initializing");
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("intervalTime"));
        columnRepeated.setCellValueFactory(new PropertyValueFactory<>("formattedRepeated"));
        datePickerFrom.setValue(LocalDate.now());
        datePickerTo.setValue(LocalDate.now());
        fieldTimeFrom.setText(ConstantUtils.DEFAULT_START_TIME);
        fieldTimeTo.setText(ConstantUtils.DEFAULT_END_TIME);
    }

    private void setCountLabel(int size) {
        labelCount.setText(size + " elements");

    }

    @FXML
    public void showTaskDialog(ActionEvent actionEvent){
        Button source = (Button) actionEvent.getSource();
        final boolean mustBeSelected = source.getId().equals("btnEdit");
        final Task selectedTask = getSelectedTask(mustBeSelected);
        if (selectedTask == null && mustBeSelected) {
            return;
        }

        final NewEditController controller;
        if (source.getId().equals("btnEdit")) {
            controller = new EditController("Edit task", selectedTask);
        } else {
            controller = new NewController("New Task");
        }
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/new-edit-task.fxml"));
            loader.setController(controller);
            Parent root = loader.load();//getClass().getResource("/fxml/new-edit-task.fxml"));

            controller.setService(service);
            controller.setTasksList(tasksList);

            stage.setScene(new Scene(root, 600, 350));
            stage.setResizable(false);
            stage.initOwner(Main.primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);//??????
            stage.show();
            controller.setCurrentStage(stage);
        }
        catch (IOException e){
            log.error("Error loading new-edit-task.fxml");
        }
    }
    @FXML
    public void deleteTask(){
        Task toDelete = (Task)tasks.getSelectionModel().getSelectedItem();
        if (toDelete == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No task selected.", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        tasksList.remove(toDelete);
        TaskIO.rewriteFile(tasksList);
    }
    @FXML
    public void showDetailedInfo(){
        try {
            Task currentTask = getSelectedTask(true);
            if (currentTask == null) {
                return;
            }
            Stage stage = new Stage();
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/fxml/task-info.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 550, 350));
            stage.setResizable(false);
            stage.setTitle("Info");
            stage.initModality(Modality.APPLICATION_MODAL);//??????
            infoStage = stage;
            stage.show();
        }
        catch (IOException e){
            log.error("error loading task-info.fxml");
        }
    }
    @FXML
    public void showFilteredTasks(){
        Date start = getDateFromFilterField(datePickerFrom.getValue(), fieldTimeFrom.getText());
        Date end = getDateFromFilterField(datePickerTo.getValue(), fieldTimeTo.getText());

        Iterable<Task> filtered =  service.filterTasks(start, end);

        ObservableList<Task> observableTasks = FXCollections.observableList((ArrayList)filtered);
        tasks.setItems(observableTasks);
        setCountLabel(observableTasks.size());
    }
    private Date getDateFromFilterField(LocalDate localDate, String time){
        Date date = dateService.getDateValueFromLocalDate(localDate);
        return dateService.getDateMergedWithTime(time, date);
    }
    @FXML
    public void resetFilteredTasks(){
        tasks.setItems(tasksList);
        setCountLabel(tasksList.size());
    }

    private Task getSelectedTask(final boolean showWarning) {
        Task currentTask = (Task)Controller.mainTable.getSelectionModel().getSelectedItem();
        if (currentTask == null && showWarning) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No task selected.", ButtonType.CLOSE);
            alert.showAndWait();
            return null;
        }
        return currentTask;
    }
}
