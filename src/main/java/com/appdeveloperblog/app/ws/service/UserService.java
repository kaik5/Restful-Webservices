package com.appdeveloperblog.app.ws.service;

import java.util.List;

import com.appdeveloperblog.app.ws.share.dto.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
	UserDto createUser(UserDto user);
	UserDto updateUser(String id, UserDto user);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);

}
