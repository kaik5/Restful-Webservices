package com.appdeveloperblog.app.ws.io.respositories;

import com.appdeveloperblog.app.ws.io.entity.PasswordResetTokenEntity;

import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByToken(String token);
}
