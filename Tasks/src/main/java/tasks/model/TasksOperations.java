package tasks.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.controller.NewEditController;

import java.util.*;

public class TasksOperations {

    private static final Logger log = Logger.getLogger(TasksOperations.class.getName());

    public List<Task> tasks;

    public TasksOperations(ObservableList<Task> tasksList){
        tasks = new ArrayList<>();
        this.tasks.addAll(tasksList);
    }

    public TasksOperations(){
    }

    public Iterable<Task> incoming(Date start, Date end) {
        return incoming(FXCollections.observableArrayList(this.tasks), start, end);
    }

    public Iterable<Task> incoming(ObservableList<Task> tasksList, Date start, Date end){
        tasks=new ArrayList<>();
        tasks.addAll(tasksList);
        System.out.println(start);
        System.out.println(end);
        log.info(String.format("Incoming task: %s -> %s", start, end));
        ArrayList<Task> incomingTasks = new ArrayList<>();
        if (start.after(end)) {
            log.warn(String.format("%s is after %s", start, end));
            return incomingTasks;
        }
        for (Task t : tasks) {
            Date nextTime = t.nextTimeAfter(start);
            if (nextTime != null) {
                if (nextTime.before(end) && nextTime.after(start) || start.equals(nextTime)) {
                    incomingTasks.add(t);
                    log.info(t.getTitle());
                }
            } else {
                log.info(String.format("%s is not fit", t.getTitle()));
            }
        }
        if (incomingTasks.size() == 0) {
            log.info("No task for this period.");
        }
        return incomingTasks;
    }

}
