package dao;

import models.Category;
import models.Task;
import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;

public class Sql2oCategoryDaoTest {
    private Sql2oCategoryDao categoryDao;
    private Sql2oTaskDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        categoryDao = new Sql2oCategoryDao(sql2o);
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Category category = new Category("Work");
        int originalCategoryId = category.getId();
        categoryDao.add(category);
        assertNotEquals(originalCategoryId, category.getId());
    }

    @Test
    public void existingCategoriesCanBeFoundById() throws Exception{
        Category category = new Category("Work");
        categoryDao.add(category);
        Category foundCategory = categoryDao.findById(category.getId());
        assertEquals(category, foundCategory);
    }

    @Test
    public void getAllCorrectlyGetsAll() throws Exception{
        Category category = new Category("Work");
        categoryDao.add(category);
        Category categoryTwo = new Category("play");
        categoryDao.add(categoryTwo);
        assertEquals(2, categoryDao.getAll().size());
    }

    @Test
    public void getAllReturnsNoCategoriesIfNoneAdded() throws Exception{
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void updateUpdatesCorrectly() throws Exception{
        Category category = new Category("Work");
        categoryDao.add(category);
        categoryDao.update(category.getId(), "Do Work");
        Category updatedCategory = categoryDao.findById(category.getId());
        assertEquals("Do Work", updatedCategory.getName());
    }

    @Test
    public void deleteDeletesCategoryCorrectly() {
        Category category = new Category("Work");
        categoryDao.add(category);
        categoryDao.deleteById(category.getId());
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void clearAllCategories_clearsAllCategories() {
        Category category = new Category("Work");
        Category category1 = new Category("School");
        categoryDao.add(category);
        categoryDao.add(category1);
        categoryDao.clearAllCategories();
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() {
        Category category = new Category("chores");
        categoryDao.add(category);
        int categoryId = category.getId();
        Task newTask = new Task("mow the lawn", categoryId);
        Task otherTask = new Task("pull weeds", categoryId);
        Task thirdTask = new Task("trim hedge", categoryId);
        taskDao.add(newTask);
        taskDao.add(otherTask);
        assertEquals(2, categoryDao.getAllTasksByCategory(categoryId).size());
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(newTask));
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(otherTask));
        assertFalse(categoryDao.getAllTasksByCategory(categoryId).contains(thirdTask)); //things are accurate!
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }
}