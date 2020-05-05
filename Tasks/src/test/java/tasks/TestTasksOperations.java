package tasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.model.Task;
import tasks.model.TasksOperations;
import tasks.services.DateService;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class TestTasksOperations {
    private static TasksOperations tasksOperations;
    static ObservableList<Task> tasks = FXCollections.observableArrayList();

    @BeforeAll
    public static void setUp() {
        tasksOperations = new TasksOperations(tasks);
    }

    @AfterEach
    public void down() {
        tasks.clear();
    }

    @Test
    public void invalidEndBeforeStart() {
        Date start = DateService.addMonths(2);
        Date end = new Date();
        assertTrue(!tasksOperations.incoming(start, end).iterator().hasNext());
    }

    @Test
    public void emptyList() {
        Date start = DateService.addMonths(-2);
        Date end = new Date();
        assertTrue(!tasksOperations.incoming(start, end).iterator().hasNext());
    }

    @Test
    public void beginningOfTaskIsEqualsEndInput() {
        Date start = DateService.addMonths(-2);
        Date end = DateService.addMonths(-1);
        Task task = new Task("Ilie Boss", DateService.addMonths(-1),new Date());
        task.setActive(true);
        tasks.add(task);
        tasksOperations = new TasksOperations(tasks);

        assertTrue(!tasksOperations.incoming(start, end).iterator().hasNext());
    }

    @Test
    public void validTask() {
        Date start = new Date();
        Date end = DateService.addMonths(3);

        Task task = new Task("Ilie Boss", DateService.addMonths(-1), DateService.addMonths(1));
        task.setActive(true);
        tasks.add(task);
        Task task2 = new Task("Ilie Boss", DateService.addMonths(-1), DateService.addMonths(2));
        task2.setActive(true);
        tasks.add(task2);
        tasksOperations = new TasksOperations(tasks);
        assertTrue(tasksOperations.incoming(start, end).iterator().hasNext());
    }

    @Test
    public void invalidRepeted() {
        Date start = new Date();
        Date end = DateService.addMonths(1);
        tasks.clear();

        Task task = new Task("Ilie Boss", DateService.addMonths(2), DateService.addMonths(3), 1000);
        task.setActive(true);
        tasks.add(task);

        tasksOperations = new TasksOperations(tasks);
        assertTrue(!tasksOperations.incoming(start, end).iterator().hasNext());
    }
}
