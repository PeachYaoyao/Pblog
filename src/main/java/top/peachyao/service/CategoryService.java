package top.peachyao.service;

import top.peachyao.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList();
    List<Category> getCategoryNameList();
    void saveCategory(Category category);
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
}
