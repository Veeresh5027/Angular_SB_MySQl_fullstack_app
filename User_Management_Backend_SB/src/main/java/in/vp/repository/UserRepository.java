package in.vp.repository;

import in.vp.enttity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findAllByOrderByCreatedAtDesc();
    Optional<UserEntity> findByEmail(String email);
}

