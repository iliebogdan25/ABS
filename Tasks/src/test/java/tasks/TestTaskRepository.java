package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tasks.model.Task;
import tasks.repos.TasksRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TestTaskRepository {


    @Test
    public void testAdd() {
        TasksRepository tasksRepository = mock(TasksRepository.class);
        tasksRepository.add(new Task("", new Date(), new Date()));
        assertEquals(tasksRepository.size(), 0);
    }

    @Test
    public void testRemove() {
        TasksRepository tasksRepository = new TasksRepository();
        TasksRepository spyTaskRepo = spy(tasksRepository);

        Mockito.doNothing().when(spyTaskRepo).remove(anyObject());
        Task task = new Task("", new Date(), new Date());
        spyTaskRepo.add(task);
        assertEquals(spyTaskRepo.size(), 1);

        spyTaskRepo.remove(task);
        assertEquals(spyTaskRepo.size(), 1);
    }
}
