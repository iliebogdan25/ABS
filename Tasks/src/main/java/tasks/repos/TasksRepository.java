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

    public void remove(final Task task) {
        taskList.remove(task);
    }

    public void add(final Task task) {
        taskList.add(task);
    }

    public List<Task> getTaskList() {
        return new ArrayList<>(taskList);
    }

    public int size() {
        return taskList.size();
    }
}
