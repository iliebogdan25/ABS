package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.model.Task;
import tasks.model.TasksOperations;
import tasks.model.Validator;
import tasks.repos.TasksRepository;

import java.util.Date;

import static tasks.services.DateService.addMonths;
import static tasks.services.DateService.addYears;

public class TasksService {
    private TasksRepository tasksRepository;
    private TasksOperations tasksOperations;
    private Validator validator = new Validator();

    public TasksService(TasksRepository tasksRepository){
        tasksOperations = new TasksOperations();
        this.tasksRepository = tasksRepository;
    }

    public TasksService(TasksRepository tasksRepository, TasksOperations tasksOperations){
        this.tasksRepository = tasksRepository;
        this.tasksOperations = tasksOperations;
    }


    public ObservableList<Task> getObservableList(){
        return FXCollections.observableArrayList(tasksRepository.getTaskList());
    }
    public String getIntervalInHours(Task task){
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }
    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }
    public int parseFromStringToSeconds(String stringTime){//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        return (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
    }

    public Iterable<Task> filterTasks(Date start, Date end){
        return tasksOperations.incoming(getObservableList(), start, end);
    }

    public Task saveTask(final String title, final Date start, final Date end, final int interval, final boolean isActive) {
        return saveTask(title, start, end, interval, isActive, validator);
    }

    public Task saveTask(final String title, final Date start, final Date end, final int interval, final boolean isActive, final Validator validator) {
        if (interval < 1) {
            throw new IllegalArgumentException("Interval must be > 0");
        }
        validator.validateCommonArguments(title, start, end);
        Task task = new Task(title, start, end, interval);
        task.setActive(isActive);
        tasksRepository.add(task);
        return task;
    }


    public int getSize() {
        return tasksRepository.size();
    }

    public void remove(Task task) {
        tasksRepository.remove(task);
    }
}
