package com.blog.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.app.config.AppConstants;
import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.payloads.PostResponse;
import com.blog.app.services.FileService;
import com.blog.app.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("project.image")
	private String path;

//	POST
	@PostMapping("add/{userId}/{catId}")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto pd, @PathVariable Integer userId,
			@PathVariable Integer catId) {

		PostDto createdPost = this.postService.createPost(pd, userId, catId);

		return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
	}

//	PUT
	@PutMapping("update{id}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto pd, @PathVariable Integer id) {

		PostDto updatedPost = this.postService.updatePost(pd, id);
		return ResponseEntity.ok(updatedPost);
	}

//	DELETE
	@DeleteMapping("delete{id}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer id) {
		this.postService.deletePost(id);

		return new ResponseEntity<>(new ApiResponse("Post Deleted Successfully", true), HttpStatus.OK);
	}

//	GET
//	Get All posts
	@GetMapping("getall")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pNo,
			@RequestParam(value = "pSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "order", defaultValue = AppConstants.ORDER, required = false) String order) {

		return ResponseEntity.ok(this.postService.getAllPosts(pNo, pSize, sortBy, order));
	}

//	Get single post

	@GetMapping("post{id}")
	public ResponseEntity<PostDto> getPostByPostId(@PathVariable Integer id) {

		return ResponseEntity.ok(this.postService.getPostByPostId(id));
	}

//	Get by user
	@GetMapping("byuser{userId}")
	public ResponseEntity<PostResponse> getAllByUser(@PathVariable Integer userId,
			@RequestParam(value = "pNo", defaultValue = "0", required = false) Integer pNo,
			@RequestParam(value = "pSize", defaultValue = "10", required = false) Integer pSize) {

		return ResponseEntity.ok(this.postService.getAllPostsByUser(userId, pNo, pSize));
	}

//	Get by category
	@GetMapping("incat{catId}")
	public ResponseEntity<PostResponse> getAllByCat(@PathVariable Integer catId,
			@RequestParam(value = "pNo", defaultValue = "0", required = false) Integer pNo,
			@RequestParam(value = "pSize", defaultValue = "10", required = false) Integer pSize) {

		return ResponseEntity.ok(this.postService.getAllPostsByCat(catId, pNo, pSize));
	}

//	Get by search
	@GetMapping("search/{keyword}")
	public ResponseEntity<List<PostDto>> getByTitleContaining(@PathVariable String keyword) {

		return ResponseEntity.ok(this.postService.searchByTitleContaining(keyword));
	}

//	Post image
	@PostMapping("post{postId}/image")
	public ResponseEntity<PostDto> imageUpload(@PathVariable Integer postId, @RequestParam("image") MultipartFile image)
			throws IOException {

		PostDto postDto = this.postService.getPostByPostId(postId);
		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);

		return ResponseEntity.ok(this.postService.updatePost(postDto, postId));
	}

//	Get Image
	@GetMapping(value = "post{postId}/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable Integer postId, @PathVariable String imageName, HttpServletResponse res)
			throws IOException {
		InputStream is = this.fileService.getResource(path, imageName);
		res.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(is, res.getOutputStream());
	}
}
