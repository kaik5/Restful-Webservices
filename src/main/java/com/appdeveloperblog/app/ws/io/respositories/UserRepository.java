package com.appdeveloperblog.app.ws.io.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByUserId(String userId);
	UserEntity findByEmail(String email);
	//UserEntity findByLastName(String lastName);


}
