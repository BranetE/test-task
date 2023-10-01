package com.example.test.repository;

import com.example.test.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where birth_date between :before and :after",
            nativeQuery = true)
    List<User> findByBirthDates(@Param("before") LocalDate before,
                                @Param("after") LocalDate after);
}
