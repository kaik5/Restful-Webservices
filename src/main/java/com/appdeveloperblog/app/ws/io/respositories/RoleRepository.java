package com.appdeveloperblog.app.ws.io.respositories;

import com.appdeveloperblog.app.ws.io.entity.RoleEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long>{
    RoleEntity findByName(String name);
    
}
