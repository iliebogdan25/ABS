package tasks;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.Task;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestTask {
    @Test
    public void testGetter() {
        Task task = new Task("ASD", new Date(), new Date());
        Task spyTask = Mockito.spy(task);

        Mockito.doReturn("ILIE").when(spyTask).getTitle();

        assertEquals(spyTask.getTitle(), "ILIE");
    }

    @Test
    public void testIsRepeted() {
        Task task = new Task("ASD", new Date(), new Date());
        Task spyTask = Mockito.spy(task);

        Mockito.doReturn(false).when(spyTask).isRepeated();
        spyTask.setTime(new Date(), new Date(), 101);

        assertFalse(spyTask.isRepeated());
    }
}
