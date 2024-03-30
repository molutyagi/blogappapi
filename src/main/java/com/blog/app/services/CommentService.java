package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.CommentDto;

public interface CommentService {

	List<CommentDto> getAllComments();

	CommentDto postComment(CommentDto commentDto, Integer postId, Integer userId);

	void deleteComment(Integer cId);
}
