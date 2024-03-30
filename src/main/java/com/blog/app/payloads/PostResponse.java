package com.blog.app.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponse {

	private List<PostDto> content;
	private Integer pNo;
	private Integer pSize;
	private long totalElements;
	private Integer totalPages;
	private boolean isLastPage;

}
