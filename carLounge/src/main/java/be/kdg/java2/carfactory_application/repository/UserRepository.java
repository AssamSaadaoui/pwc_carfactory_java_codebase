package be.kdg.java2.carfactory_application.repository;


import be.kdg.java2.carfactory_application.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
