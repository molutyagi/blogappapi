package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.PostDto;
import com.blog.app.payloads.PostResponse;

public interface PostService {
	PostDto createPost(PostDto postDto, Integer userId, Integer catId);

	PostDto updatePost(PostDto postDto, Integer id);

	void deletePost(Integer id);

	PostDto getPostByPostId(Integer id);

	PostResponse getAllPostsByCat(Integer catId, Integer pNo, Integer pSize);

	PostResponse getAllPostsByUser(Integer userId, Integer pNo, Integer pSize);

	PostResponse getAllPosts(Integer pNo, Integer pSize, String sortBy, String order);
	
	List<PostDto> searchByTitleContaining(String keyword);

}
