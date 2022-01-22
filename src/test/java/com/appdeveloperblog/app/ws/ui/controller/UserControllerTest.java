// package com.appdeveloperblog.app.ws.ui.controller;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;

// import com.appdeveloperblog.app.ws.service.impl.UserServiceImpl;
// import com.appdeveloperblog.app.ws.share.dto.AddressDTO;
// import com.appdeveloperblog.app.ws.share.dto.UserDto;
// import com.appdeveloperblog.app.ws.ui.model.response.UserRest;

// import static org.mockito.Mockito.when;

// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// class UserControllerTest {
	
// 	@InjectMocks
// 	UserController userController;
	
// 	@Mock
// 	UserServiceImpl userService;

// 	UserDto userDto;
// 	final String USER_ID = "bfhry47fas463gdh";
// 	@BeforeEach
// 	void setUp() throws Exception {
// 		MockitoAnnotations.openMocks(this);
				
// 		userDto = new UserDto();
//         userDto.setFirstName("FSA");
//         userDto.setLastName("FAS");
//         userDto.setEmail("test@test.com");
//         userDto.setEmailVerificationStatus(Boolean.FALSE);
//         userDto.setEmailVerificationToken(null);
//         userDto.setUserId(USER_ID);
//         userDto.setAddresses(getAddressesDto());
//         userDto.setEncryptedPassword("xcf58tugh47");
		
// 	}

	
// 	@Test
// 	final void testGetUser() {
// 		when(userService.getUserByUserId(anyString())).thenReturn(null);
// 		UserRest userRest = userController.getUser(USER_ID);
// 		assertNotNull(userRest);
// 		assertEquals(USER_ID, userRest.getUserId());
// 		assertEquals(userDto.getFirstName(), userRest.getFirstName());
// 		assertEquals(userDto.getAddresses(), userRest.getAddresses());
// 		assertEquals(userDto.getEmail(), userRest.getEmail());



		
// 	}

// 	private List<AddressDTO> getAddressesDto() {
// 		AddressDTO addressDto = new AddressDTO();
// 		addressDto.setType("shipping");
// 		addressDto.setCity("LA");
// 		addressDto.setState("CA");
// 		addressDto.setZip("ABC123");
// 		addressDto.setStreet("123 Street name");

// 		AddressDTO billingAddressDto = new AddressDTO();
// 		billingAddressDto.setType("billling");
// 		billingAddressDto.setCity("NYC");
// 		billingAddressDto.setState("NY");
// 		billingAddressDto.setZip("ABC123");
// 		billingAddressDto.setStreet("123 Street name");

// 		List<AddressDTO> addresses = new ArrayList<>();
// 		addresses.add(addressDto);
// 		addresses.add(billingAddressDto);

// 		return addresses;

// 	}


// }
