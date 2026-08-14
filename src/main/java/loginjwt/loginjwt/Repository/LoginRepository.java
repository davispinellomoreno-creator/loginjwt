package loginjwt.loginjwt.Repository;

import loginjwt.loginjwt.Model.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity,Long> {
}
