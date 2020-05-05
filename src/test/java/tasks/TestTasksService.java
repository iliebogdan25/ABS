package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import tasks.model.Task;
import tasks.repos.TasksRepository;
import tasks.services.DateService;
import tasks.services.TasksService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

public class TestTasksService {
    static TasksService tasksService;
    final boolean isActive = true;

    @BeforeAll
    public static void setup() {
        List<Task> tasks = new ArrayList<>();
        tasksService = new TasksService(new TasksRepository(tasks));
    }

    private static Stream<Arguments> invalidStartTime() {
        return Stream.of(
                Arguments.of(DateService.addMonths(-2, -1), new Date()),
                Arguments.of(DateService.addYears(2, 1), DateService.addYears(1)));
    }

    private static Stream<Arguments> validStartTime() {
        Date oneMonthAgo = DateService.addMonths(-1);
        oneMonthAgo.setMinutes(oneMonthAgo.getMinutes() + 1);
        return Stream.of(
                Arguments.of(oneMonthAgo, new Date()),
                Arguments.of(DateService.addMonths(-1, 1), new Date()),
                Arguments.of(DateService.addYears(2), DateService.addYears(2)),
                Arguments.of(DateService.addYears(2, -1), DateService.addYears(2)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @Tag("title")
    void testInvalidTitle(String title) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> createTaskInService(title));
        assertEquals("Title must not be empty", exception.getMessage());
    }

    @Test
    @Tag("title")
    public void testValidTitle() {
        final String title = "A";

        assertNotNull(createTaskInService(title));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, Integer.MAX_VALUE})
    @Tag("interval")
    public void testValidInterval(int interval) {
        assertNotNull(createTaskInService(interval));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    @Tag("interval")
    public void testInvalidInterval(int interval) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> createTaskInService(interval));
        assertEquals("Interval must be > 0", exception.getMessage());
    }

    @Test
    @DisplayName("Test that IllegalArgumentException throws when start is after end.")
    @Tag("time")
    public void testInvalidStartAfterEndTime() {
        Date start = new Date();
        Date end = DateService.addMonths(-1);
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> createTaskInService(start, end));
        assertEquals("The end date must be no more than one month earlier than the current date.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidStartTime")
    @Tag("time")
    public void testInvalidStartTime(final Date start, final Date end) {
        assertThrows(IllegalArgumentException.class, () -> createTaskInService(start, end));
    }

    @ParameterizedTest
    @MethodSource("validStartTime")
    @Tag("time")
    public void testValidStartTime(final Date start, final Date end) {
        assertNotNull(createTaskInService(start, end));
    }

    public Task createTaskInService(final String title) {
        Date start = new Date();
        Date end = DateService.addYears(1);

        return createTaskInService(title, start, end, 100);
    }

    public Task createTaskInService(final Date start, final Date end) {
        return createTaskInService("ANA", start, end, 100);
    }

    public Task createTaskInService(final String title, Date start, Date end, int interval) {
        return tasksService.saveTask(title, start, end, interval, isActive);
    }

    public Task createTaskInService(final int interval) {
        Date start = new Date();
        Date end = DateService.addYears(1);

        return createTaskInService("ANA", start, end, interval);
    }

    @Test
    public void testGetSize() {
        TasksRepository tasksRepository = new TasksRepository();
        TasksRepository spyTaskRepo = spy(tasksRepository);

        TasksService tasksService = new TasksService(spyTaskRepo);
        Mockito.doReturn(2).when(spyTaskRepo).size();

        assertEquals(tasksService.getSize(), 2);
    }

    @Test
    public void testGetObservableList() {
        TasksRepository tasksRepository = new TasksRepository();
        TasksRepository spyTaskRepo = spy(tasksRepository);

        Task task = new Task("Ilie", new Date(), new Date());
        Mockito.doReturn(Arrays.asList(task)).when(spyTaskRepo).getTaskList();
        TasksService tasksService = new TasksService(spyTaskRepo);

        assertTrue(tasksService.getObservableList().contains(task));
    }
}
