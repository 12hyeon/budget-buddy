package hyeon.buddy.repository;

import hyeon.buddy.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByTitleContaining(String title);
    long count();
}
