package com.appdeveloperblog.app.ws.ui.controller;

import com.appdeveloperblog.app.ws.ui.model.request.LoginRequestModel;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;




@RestController
public class AuthenticationController {
    @ApiOperation("User login")
    @ApiResponses(value = {
    @ApiResponse(code = 200, 
            message = "Response Headers", 
            responseHeaders = {
                @ResponseHeader(name = "authorization", 
                        description = "Bearer <JWT token value here>"),
                @ResponseHeader(name = "userId", 
                        description = "<Public User ID value here>")
            })  
    })
	
    
   @PostMapping("/users/login")
    public void fakeLogin(@RequestBody LoginRequestModel loginRequestModel)
    {
        throw new IllegalStateException("This method should not be called. Spring Security.");
    }
    
}
