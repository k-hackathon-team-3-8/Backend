package org.wakeupu.wakeupu.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.wakeupu.wakeupu.entity.member.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);
    Optional<RefreshToken> deleteByKey(String key);

}
