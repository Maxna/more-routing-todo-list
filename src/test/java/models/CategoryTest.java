package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {
    @Test
    public void categoryInstantiatesCorrectly() throws Exception{
        Category category = new Category("Work");
        assertEquals(true, category instanceof Category);
    }

    @Test
    public void getName_getsNameCorrectly() {
        Category category = new Category("Work");
        assertEquals("Work", category.getName());
    }

}