package com.blog.app.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.blog.app.entities.Category;
import com.blog.app.entities.Comment;
import com.blog.app.entities.User;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {

	private Integer postId;

	private String title;

	private String content;

	private String imageName = "default.png";

	private Date addedDate = new Date();

	private UserDto user;

	private CategoryDto category;

	private Set<CommentDto> comments = new HashSet<>();
}
