package loginjwt.loginjwt.Repository;

import loginjwt.loginjwt.Model.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {

    boolean existsByEmail(String email);

    Optional<LoginEntity> findByEmail(String email);

}