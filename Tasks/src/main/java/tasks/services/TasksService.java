package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.model.Task;
import tasks.model.TasksOperations;
import tasks.repos.TasksRepository;

import java.util.Date;
import java.util.List;

import static tasks.services.DateService.addMonths;
import static tasks.services.DateService.addYears;

public class TasksService {
    private TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository){
        this.tasksRepository = tasksRepository;
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
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        return tasksOps.incoming(start, end);
    }

    public Task saveTask(final String title, final Date start, final Date end, final int interval, final boolean isActive) {
        if (interval < 1) {
            throw new IllegalArgumentException("Interval must be > 0");
        }
        validateCommonArguments(title, start, end);
        Task task = new Task(title, start, end, interval);
        task.setActive(isActive);
        return task;
    }

    public void validateCommonArguments(final String title, final Date start, final Date end) {
        if (start.before(addMonths(-1))) {
            throw new IllegalArgumentException("The start date must be no more than one month earlier than the current date.");
        }
        if (end.before(addMonths(-1))) {
            throw new IllegalArgumentException("The end date must be no more than one month earlier than the current date.");
        }
        if (end.after(addYears(2))) {
            throw new IllegalArgumentException("The end date must be no more than 2 years later than the start date.");
        }
        if (start.after(addYears(2))) {
            throw new IllegalArgumentException("The start date must be no more than 2 years later than the start date.");
        }
        if (end.before(start)) {
            throw new IllegalArgumentException("Start must be before End.");
        }
        if (title == null || (title.trim().isEmpty())) {
            throw new IllegalArgumentException("Title must not be empty");
        }
    }


}
