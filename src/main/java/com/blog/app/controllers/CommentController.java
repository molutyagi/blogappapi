package com.blog.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.config.AppConstants;
import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CommentDto;
import com.blog.app.payloads.PostResponse;
import com.blog.app.services.CommentService;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@GetMapping("comments/getall")
	public ResponseEntity<List<CommentDto>> getAllComments() {

		return ResponseEntity.ok(this.commentService.getAllComments());
	}

	@PostMapping("post/{postId}/user/{userId}")
	public ResponseEntity<CommentDto> postComment(@RequestBody CommentDto commentDto, @PathVariable Integer postId,
			@PathVariable Integer userId) {

		CommentDto createdComment = this.commentService.postComment(commentDto, postId, userId);

		return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);
	}

	@DeleteMapping("comment/{cId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer cId) {
		this.commentService.deleteComment(cId);

		return new ResponseEntity<>(new ApiResponse("Comment Deleted Successfully", true), HttpStatus.OK);
	}
}
