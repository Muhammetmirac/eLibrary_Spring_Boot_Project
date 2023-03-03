package com.runners.repository;

import com.runners.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();
    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);
    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(Long id);

    @Modifying
    @Query("UPDATE User u SET u.name=:nname,u.surname=:ssurname, u.email=:eemail, u.address=:address, u.phoneNumber=:phoneNumber where u.id=:idd ")
    void update(@Param("idd") Long id,
                @Param("nname") String name,
                @Param("ssurname") String surname,
                @Param("address") String address,
                @Param("phoneNumber")String phoneNumber,
                @Param("eemail")String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserById(Long id);
}
