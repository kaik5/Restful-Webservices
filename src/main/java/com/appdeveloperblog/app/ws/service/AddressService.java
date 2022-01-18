package com.appdeveloperblog.app.ws.service;

import java.util.List;

import com.appdeveloperblog.app.ws.share.dto.AddressDTO;

public interface AddressService 
{
    List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String userId);

    
}
