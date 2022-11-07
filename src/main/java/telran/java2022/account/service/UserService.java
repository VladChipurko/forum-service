package telran.java2022.account.service;

import telran.java2022.account.dto.LoginPasswordDto;
import telran.java2022.account.dto.RolesChangeDto;
import telran.java2022.account.dto.UserDto;
import telran.java2022.account.dto.UserRegisterDto;
import telran.java2022.account.dto.UserUpdateDto;

public interface UserService {

	UserDto register(UserRegisterDto userRegisterDto);
	
	UserDto login(LoginPasswordDto loginPasswordDto);
	
	UserDto deleteUser(String login);
	
	UserDto updateUser(String login, UserUpdateDto userUpdateDto);
	
	RolesChangeDto addRole(String login, String role);
	
	RolesChangeDto deleteRole(String login, String role);
	
	void changePassword(LoginPasswordDto loginPasswordDto);
}
