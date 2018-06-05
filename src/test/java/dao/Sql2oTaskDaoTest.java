package dao;


import models.Task;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Sql2oTaskDaoTest {
    private Sql2oTaskDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open();
    }

    @Test
    public void addingTaskSetsId() throws Exception {
        Task task = setupNewTask();
        int originalTaskId = task.getId();
        taskDao.add(task);
        assertNotEquals(originalTaskId, task.getId());
    }

    @Test
    public void existingTasksCanBeFoundById() throws Exception {
        Task task = setupNewTask();
        taskDao.add(task);
        Task foundTask = taskDao.findById(task.getId());
        assertEquals(task, foundTask);
    }

    @Test
    public void getAllCorrectlyGetsAll() throws Exception {
        Task task = new Task("mow the lawn", 1);
        taskDao.add(task);
        Task task2 = new Task("do the dishes", 1);
        taskDao.add(task2);
        assertEquals(2, taskDao.getAll().size());
    }

    @Test
    public void getAllReturnsNoTasksIfNoneAdded() throws Exception {
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void updateUpdatesCorrectly() throws Exception {
        Task task = new Task("mow the lawn", 1);
        taskDao.add(task);
        taskDao.update(task.getId(), "do the dishes", 1);
        Task updatedTask = taskDao.findById(task.getId());
        assertEquals("do the dishes", updatedTask.getDescription());
    }

    @Test
    public void deleteDeletesTaskCorrectly() throws Exception {
        Task task = new Task("mow the lawn", 1);
        taskDao.add(task);
        taskDao.deleteById(task.getId());
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void clearAllTasks_clearsAllTasks() throws Exception {
        Task task1 = new Task("mow the lawn", 1);
        Task task2 = new Task("do the dishes", 1);
        taskDao.add(task1);
        taskDao.add(task2);
        taskDao.clearAllTasks();
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void categoryIdIsReturnedCorrectly() throws Exception {
        Task task = new Task("mow the lawn", 1);
        int originalCatId = task.getCategoryId();
        taskDao.add(task);
        assertEquals(originalCatId, taskDao.findById(task.getId()).getCategoryId());
    }



    //helper method
    public Task setupNewTask(){
        return new Task("mow the lawn", 1);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

}
