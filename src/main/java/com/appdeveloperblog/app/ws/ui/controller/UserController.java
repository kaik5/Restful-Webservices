package com.appdeveloperblog.app.ws.ui.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.appdeveloperblog.app.ws.Exceptions.UserServiceException;
import com.appdeveloperblog.app.ws.service.AddressService;
import com.appdeveloperblog.app.ws.service.UserService;
import com.appdeveloperblog.app.ws.share.dto.AddressDTO;
import com.appdeveloperblog.app.ws.share.dto.UserDto;
import com.appdeveloperblog.app.ws.ui.model.request.UserDetailRequestModel;
import com.appdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController 
{
	@Autowired
	UserService userService;

	@Autowired
	AddressService addressesService;

	@Autowired
	AddressService addressService;

	@GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}  )
	public UserRest getUser(@PathVariable String id)
	{
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} , produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailRequestModel userDetails) throws Exception
	{
		UserRest returnValue = new UserRest();
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		//UserDto userDto = new UserDto(); 
		//BeanUtils.copyProperties(userDetails, userDto);
		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(path = "/{id}", produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}  )
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailRequestModel userDetails)
	{
		UserRest returnValue = new UserRest();
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		UserDto userDto = new UserDto(); 
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
		
	}
	@DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}  )
	public OperationStatusModel deleteUser(@PathVariable String id)
	{
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		returnValue.setOperationName(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping(produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	// params: spacific page and number  
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "25") int limit)
	{
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);

		for(UserDto userDto : users)
		{
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;

	}

	// endpoint: .../userID/addresses
	@GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}  )
	public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String id)
	{
		List<AddressesRest> returnValue = new ArrayList<>();

		List<AddressDTO> addressDto = addressesService.getAddresses(id);

		if(addressDto != null && !addressDto.isEmpty())
		{
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressDto, listType); 

			for(AddressesRest addressesRest: returnValue )
			{
				Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUserAddress(id, addressesRest.getAddressId()))
				//.slash(userId)
				//.slash("addresses")
				//.slash(addressId)
				.withSelfRel();

				addressesRest.add(selfLink);
				
			}
			
		}

		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
		.getUserAddresses(id))
		//.slash(userId)
		//.slash("addresses")
		//.slash(addressId)
		.withSelfRel();


		return CollectionModel.of(returnValue, userLink, selfLink);


	}

		// endpoint: .../userID/addresses/addressId
		@GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}  )
		public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId)
		{
			AddressDTO addressDto = addressService.getAddress(addressId);
	
			ModelMapper modelMapper = new ModelMapper();
			AddressesRest returnValue =  modelMapper.map(addressDto, AddressesRest.class);

			// localhost:8080/users/userID/addresses
			Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
			Link userAddressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
			//.slash(userId)
			//.slash("addresses")
			.withRel("addresses");
			
			Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
			.getUserAddress(userId, addressId))
			//.slash(userId)
			//.slash("addresses")
			//.slash(addressId)
			.withSelfRel();

			// returnValue.add(userLink);
			// returnValue.add(userAddressLink);
			// returnValue.add(selfLink);

			
			return EntityModel.of(returnValue, Arrays.asList(userLink, userAddressLink, selfLink));
	
		}

			
     	//http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
		@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
		public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) 
		{

			OperationStatusModel returnValue = new OperationStatusModel();
			returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
			
			boolean isVerified = userService.verifyEmailToken(token);
			
			if(isVerified)
			{
				returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
			} else {
				returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
			}

			return returnValue;
		}
	
}
