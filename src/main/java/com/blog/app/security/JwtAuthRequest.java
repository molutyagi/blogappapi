package com.blog.app.security;

import lombok.Data;

@Data
public class JwtAuthRequest {

	private String name;

	private String password;

}
