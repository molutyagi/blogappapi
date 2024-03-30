package com.blog.app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.app.payloads.UserDto;

public interface UserService {

	UserDto createUser(UserDto userDto);

	UserDto updateUser(UserDto userDto, Integer id);

	UserDto getUserById(Integer id);

	List<UserDto> getAllUsers();

	void deleteUser(Integer id);
}
