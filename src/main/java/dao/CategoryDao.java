package dao;

import models.Category;
import models.Task;

import java.util.List;

public interface CategoryDao {

    List<Category> getAll();

    void add(Category category);

    Category findById(int id);

    void update(int id, String name);

    void deleteById(int id);

    void clearAllCategories();

    List<Task> getAllTasksByCategory(int categoryId);
}
