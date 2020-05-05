package tasks;

import org.junit.jupiter.api.Test;
import tasks.model.Task;
import tasks.model.ValidatorStub;
import tasks.repos.TasksRepository;
import tasks.services.DateService;
import tasks.services.TasksService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTest {
    @Test
    public void testSave() {
        TasksRepository tasksRepository = new TasksRepository();
        TasksService tasksService = new TasksService(tasksRepository);

        assertEquals (tasksRepository.size(), 0);
        assertThrows(IllegalArgumentException.class, () -> tasksService.saveTask("ASD", new Date(), DateService.addMonths(-1), 1, true));
        assertEquals (tasksRepository.size(), 0);

        tasksService.saveTask("ASD", new Date(), new Date(), 1, true);
        assertEquals (tasksRepository.size(), 1);
    }

    @Test
    public void testRemove() {
        TasksRepository tasksRepository = new TasksRepository();
        TasksService tasksService = new TasksService(tasksRepository);

        assertEquals (tasksRepository.size(), 0);
        assertThrows(IllegalArgumentException.class, () -> tasksService.saveTask("ASD", new Date(), DateService.addMonths(-1), 1, true));
        assertEquals (tasksRepository.size(), 0);

        Task task = tasksService.saveTask("ASD", new Date(), new Date(), 1, true);
        assertEquals (tasksRepository.size(), 1);

        tasksService.remove(task);
        assertEquals (tasksRepository.size(), 0);
    }
}
