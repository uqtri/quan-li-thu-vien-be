package org.example.qlthuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.qlthuvien.entity.*;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
