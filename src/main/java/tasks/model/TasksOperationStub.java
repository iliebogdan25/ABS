package tasks.model;

import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Date;

public class TasksOperationStub extends TasksOperations{

    public Iterable<Task> incoming(ObservableList<Task> tasksList, Date start, Date end) {
        return Arrays.asList(new Task("ILIE 1", new Date(), new Date()), new Task("ILIE 2", new Date(), new Date()));
    }
}
