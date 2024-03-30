package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.CategoryDto;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto catDto);

	CategoryDto updateCategory(CategoryDto catDto, Integer id);

	CategoryDto getCatById(Integer id);

	List<CategoryDto> getAllCat();

	CategoryDto deleteCategory(Integer id);
}
