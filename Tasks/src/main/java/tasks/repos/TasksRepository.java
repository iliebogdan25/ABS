package tasks.repos;

import tasks.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksRepository {
    List<Task> taskList = new ArrayList<>();

    public TasksRepository() {

    }

    public TasksRepository(final List<Task> tasks) {
        this.taskList = tasks;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
