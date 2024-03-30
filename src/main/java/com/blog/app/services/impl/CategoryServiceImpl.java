package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.repositories.CategoryRepo;
import com.blog.app.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo catRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto catDto) {
		Category category = this.modelMapper.map(catDto, Category.class);

		Category savedCategory = this.catRepo.save(category);

		return this.modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto catDto, Integer id) {
		Category category = this.catRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));

		category.setUpdateCategory(id, catDto.getName(), catDto.getDescription());

		this.catRepo.save(category);

		return this.modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryDto deleteCategory(Integer id) {
		Category category = this.catRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));

		this.catRepo.delete(category);
		return null;
	}

	@Override
	public CategoryDto getCatById(Integer id) {
		Category category = this.catRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));
		return this.modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCat() {

		List<Category> categories = this.catRepo.findAll();
		List<CategoryDto> catDtos = new ArrayList<>();

		for (Category cat : categories) {
			CategoryDto catDto = this.modelMapper.map(cat, CategoryDto.class);
			catDtos.add(catDto);
		}

		return catDtos;
	}

}
