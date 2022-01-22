// package com.appdeveloperblog.app.ws.service.impl;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.springframework.beans.BeanUtils;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import com.appdeveloperblog.app.ws.io.entity.UserEntity;
// import com.appdeveloperblog.app.ws.io.respositories.UserRepository;
// import com.appdeveloperblog.app.ws.service.UserService;
// import com.appdeveloperblog.app.ws.share.dto.UserDto;
// import com.appdeveloperblog.app.ws.shared.AmazonSES;
// import com.appdeveloperblog.app.ws.shared.Utils;

// class UserServiceImplTest {
// 	@InjectMocks
// 	UserServiceImpl userService;

// 	@Mock
// 	UserRepository userRepository;
 
// 	@Mock
// 	Utils utils;
	
// 	@Mock
// 	AmazonSES amazonSES;

// 	@Mock
// 	BCryptPasswordEncoder bCryptPasswordEncoder;
 
// 	String userId = "hhty5fsy";
// 	String encryptedPassword = "7ad74jf";
	
// 	UserEntity userEntity;


// 	@BeforeEach
// 	void setUp() throws Exception {
// 		MockitoAnnotations.openMocks(this);
// 	}


// 	@Test
// 	final void testGetUser()
// 	{
// 		UserEntity userEntity = new UserEntity();
// 		userEntity.setId(11);
// 		userEntity.setFirstName("fdsa");
// 		userEntity.setUserId("fsafasf3");
// 		userEntity.setEncryptedPassword("123fa456");
// 		userEntity.setEmail("test@test.com");
// 		userEntity.setEmailVerificationToken("teasfassassafcom");

// 		when(userRepository.findByEmail(anyString())).thenReturn(null);
// 		UserDto userDto = userService.getUser("test@test.com");

// 		assertNotNull(userDto);
// 		assertEquals("fdsa", userDto);
// 	}

// }
