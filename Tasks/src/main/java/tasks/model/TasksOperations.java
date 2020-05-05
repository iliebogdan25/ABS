package tasks.model;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.controller.NewEditController;

import java.util.*;

public class TasksOperations {

    private static final Logger log = Logger.getLogger(TasksOperations.class.getName());

    public ArrayList<Task> tasks;

    public TasksOperations(ObservableList<Task> tasksList){
        tasks=new ArrayList<>();
        tasks.addAll(tasksList);
    }

    public Iterable<Task> incoming(Date start, Date end){
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
    public SortedMap<Date, Set<Task>> calendar( Date start, Date end){
        Iterable<Task> incomingTasks = incoming(start, end);
        TreeMap<Date, Set<Task>> calendar = new TreeMap<>();

        for (Task t : incomingTasks){
            Date nextTimeAfter = t.nextTimeAfter(start);
            while (nextTimeAfter!= null && (nextTimeAfter.before(end) || nextTimeAfter.equals(end))){
                if (calendar.containsKey(nextTimeAfter)){
                    calendar.get(nextTimeAfter).add(t);
                }
                else {
                    HashSet<Task> oneDateTasks = new HashSet<>();
                    oneDateTasks.add(t);
                    calendar.put(nextTimeAfter,oneDateTasks);
                }
                nextTimeAfter = t.nextTimeAfter(nextTimeAfter);
            }
        }
        return calendar;
    }
}
