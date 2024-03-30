package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.app.entities.User;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.UserRepo;
import com.blog.app.services.UserService;
import com.blog.app.exceptions.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

//	Create user
	@Override
	public UserDto createUser(UserDto userDto) {

//		converting DTO to Entity
		User user = this.dtoToUser(userDto);

//		Saving the entity to database
		User savedUser = this.userRepo.save(user);

//		returning the DTO by converting the entity to user again
		return this.userToDto(savedUser);
	}

//	update user
	@Override
	public UserDto updateUser(UserDto ud, Integer id) {

		User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

//		setting new user details from dto to entity
		user.setUpdateUser(id, ud.getName(), ud.getEmail(), ud.getPassword(), ud.getGender(), ud.getAbout());

//		updating new user to db

		User updatedUser = this.userRepo.save(user);

//		returning the DTO by converting the entity to user again
		return this.userToDto(updatedUser);
	}

//	Get single user by id
	@Override
	public UserDto getUserById(Integer id) {

//		fetching the user from database
		User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

//		returning the DTO by converting the entity to user again
		return this.userToDto(user);
	}

//	Get all users
	@Override
	public List<UserDto> getAllUsers() {

//		fetching all user from db
		List<User> allUsers = this.userRepo.findAll();
		List<UserDto> userDtos = new ArrayList<>();

		for (User user : allUsers) {
			UserDto userDto = userToDto(user);
			userDtos.add(userDto);
		}

		return userDtos;
	}

//	Delete user
	@Override
	public void deleteUser(Integer id) {

		User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

//		deleting fetched user
		this.userRepo.delete(user);

	}

//	converting dto to user
	private User dtoToUser(UserDto ud) {
		User user = this.modelMapper.map(ud, User.class);

//		User user = new User(ud.getId(), ud.getName(), ud.getEmail(), ud.getPassword(), ud.getGender(), ud.getAbout());

		return user;
	}

//	converting user to dto
	private UserDto userToDto(User us) {

		UserDto ud = this.modelMapper.map(us, UserDto.class);

//		UserDto ud = new UserDto(us.getId(), us.getName(), us.getEmail(), us.getPassword(), us.getGender(),
//				us.getAbout());
		return ud;
	}

}
