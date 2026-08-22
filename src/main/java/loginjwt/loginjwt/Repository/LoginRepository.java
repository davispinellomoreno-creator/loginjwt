package loginjwt.loginjwt.Repository;

import loginjwt.loginjwt.Model.LoginEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity,Long> {
    static void save(User user) {
    }
}
