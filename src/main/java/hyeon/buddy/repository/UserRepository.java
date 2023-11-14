package hyeon.buddy.repository;

import hyeon.buddy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(String account);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u")
    List<Long> findAllUserIds();
}
