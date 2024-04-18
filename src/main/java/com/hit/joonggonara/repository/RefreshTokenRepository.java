package com.hit.joonggonara.repository;

import com.hit.joonggonara.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    boolean existsByRefreshToken(String RefreshToken);

}
