package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.PostDto;
import com.blog.app.payloads.PostResponse;
import com.blog.app.repositories.CategoryRepo;
import com.blog.app.repositories.PostRepo;
import com.blog.app.repositories.UserRepo;
import com.blog.app.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelmapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer catId) {

//		Get author 
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

//		Get category
		Category category = this.categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", catId));

		

		Post post = this.modelmapper.map(postDto, Post.class);
		post.setUser(user);
		post.setCategory(category);

		Post savedPost = this.postRepo.save(post);

		return this.modelmapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer id) {
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));

		post.setUpdatePost(id, postDto.getTitle(), postDto.getContent(), postDto.getImageName());

		Post updatedPost = this.postRepo.save(post);

		return this.modelmapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer id) {
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));

		this.postRepo.delete(post);

	}

	@Override
	public PostDto getPostByPostId(Integer id) {
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));

		return this.modelmapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(Integer pNo, Integer pSize, String sortBy, String order) {

		Sort sort = null;

		if (order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("ascending")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}

		Pageable p = PageRequest.of(pNo, pSize, sort);
		Page<Post> pagePost = this.postRepo.findAll(p);

		List<Post> posts = pagePost.getContent();
		List<PostDto> postDtos = new ArrayList<>();

		for (Post post : posts) {
			PostDto postDto = this.modelmapper.map(post, PostDto.class);
			postDtos.add(postDto);
		}

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPNo(pagePost.getNumber());
		postResponse.setPSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostResponse getAllPostsByCat(Integer catId, Integer pNo, Integer pSize) {

//		Get category
		Category category = this.categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", catId));

		Pageable p = PageRequest.of(pNo, pSize);
		Page<Post> pagePost = (Page<Post>) this.postRepo.findByCategory(category);

		List<Post> posts = pagePost.getContent();
		List<PostDto> postDtos = new ArrayList<>();

		for (Post post : posts) {
			PostDto postDto = this.modelmapper.map(post, PostDto.class);
			postDtos.add(postDto);
		}

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPNo(pagePost.getNumber());
		postResponse.setPSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;

	}

	@Override
	public PostResponse getAllPostsByUser(Integer userId, Integer pNo, Integer pSize) {
//		Get author 
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		Pageable p = PageRequest.of(pNo, pSize);
		Page<Post> pagePost = (Page<Post>) this.postRepo.findByUser(user);

		List<Post> posts = pagePost.getContent();
		List<PostDto> postDtos = new ArrayList<>();

		for (Post post : posts) {
			PostDto postDto = this.modelmapper.map(post, PostDto.class);
			postDtos.add(postDto);
		}

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPNo(pagePost.getNumber());
		postResponse.setPSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public List<PostDto> searchByTitleContaining(String keyword) {
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> dtos = new ArrayList<>();

		for (Post post : posts) {
			PostDto dto = this.modelmapper.map(post, PostDto.class);
			dtos.add(dto);
		}
		return dtos;
	}

}
