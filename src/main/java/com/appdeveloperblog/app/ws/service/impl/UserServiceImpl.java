package com.appdeveloperblog.app.ws.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import java.util.ArrayList;
import java.util.List;

import com.appdeveloperblog.app.ws.io.respositories.*;
import com.appdeveloperblog.app.ws.Exceptions.UserServiceException;
import com.appdeveloperblog.app.ws.io.entity.PasswordResetTokenEntity;
import com.appdeveloperblog.app.ws.io.entity.UserEntity;
import com.appdeveloperblog.app.ws.service.UserService;
import com.appdeveloperblog.app.ws.share.dto.AddressDTO;
import com.appdeveloperblog.app.ws.share.dto.UserDto;
import com.appdeveloperblog.app.ws.shared.AmazonSES;
import com.appdeveloperblog.app.ws.shared.Utils;
import com.appdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService 
{

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public UserDto createUser(UserDto user) 
	{

		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");
		
		for(int i = 0; i < user.getAddresses().size(); i ++)
		{
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}

		//BeanUtils.copyProperties(user, userEntity);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(false);
		UserEntity storedUserDetails = userRepository.save(userEntity);

		
		
		//BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

		// send email verification to users
		new AmazonSES().verifyEmail(returnValue);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
		userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());
		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email)
	{
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException("User with ID " + userId + " does not exist.");
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto updateUser(String id, UserDto user) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		UserEntity updatedUserDetails =  userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, returnValue);
		return returnValue;
	}

	@Override
	public void deleteUser(String id) 
	{
		UserEntity userEntity = userRepository.findByUserId(id);
		if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userRepository.delete(userEntity);
		
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage =  userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		for(UserEntity userEntity : users)
		{
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
	    boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

		return returnValue;

	}

	@Override
	public boolean requestPasswordReset(String email) {
        boolean returnValue = false;
        
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            return returnValue;
        }
        
        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());
        
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
        
        returnValue = new AmazonSES().sendPasswordResetRequest(
                userEntity.getFirstName(), 
                userEntity.getEmail(),
                token);
        
		return returnValue;
	}

	@Override
	public boolean resetPassword(String token, String password) {
        boolean returnValue = false;
        
        if( Utils.hasTokenExpired(token) )
        {
            return returnValue;
        }
 
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        
        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);
 
        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }
   
        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        
        return returnValue;
	}
}
