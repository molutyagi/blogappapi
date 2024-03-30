package com.blog.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.services.CategoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

//	POST - Create category
	@PostMapping("add")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto catDto) {
		CategoryDto createdCategory = this.categoryService.createCategory(catDto);

		return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
	}

//	PUT - Update category
	@PutMapping("update/{id}")
	public ResponseEntity<CategoryDto> putMethodName(@Valid @PathVariable Integer id, @RequestBody CategoryDto catDto) {
		CategoryDto updatedCategory = this.categoryService.updateCategory(catDto, id);

		return ResponseEntity.ok(updatedCategory);
	}

//	DELETE - Delete category
	@DeleteMapping("delete{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer id) {
		this.categoryService.deleteCategory(id);

		return new ResponseEntity<>(new ApiResponse("Category deleted successfully", true), HttpStatus.OK);
	}

//	GET - Get category

//	get all categories
	@GetMapping("getall")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		return ResponseEntity.ok(this.categoryService.getAllCat());
	}

//	get specified category
	@GetMapping("cat{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer id) {
		return ResponseEntity.ok(this.categoryService.getCatById(id));
	}

}
