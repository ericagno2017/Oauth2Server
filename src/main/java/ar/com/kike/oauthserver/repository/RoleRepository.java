package ar.com.kike.oauthserver.repository;

import ar.com.kike.oauthserver.model.Role;
import ar.com.kike.oauthserver.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}