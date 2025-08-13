package com.the.mudpot.repository;

import com.the.mudpot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserName(String userName);

    User  findByEmailAddress(String emailAddress);

    Page<User> findAllByDeleted(boolean deleted, Pageable pageable);

    Optional<User> findByIdAndDeleted(String id, boolean deleted);

    Page<User> findByEmailAddressContainingIgnoreCase(String search, Pageable pageable);
    Page<User> findByEmailAddressContainingIgnoreCaseAndStatusOrderByCreatedAtDesc(String search, String status, Pageable pageable);

    Optional<User> findByIdAndDeletedFalse(String id);
    Optional<User> findByEmailAddressAndDeleted(String emailAddress, boolean deleted);

    Page<User> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    @Query("SELECT u FROM User u Order By u.createdAt Desc")
    Page<User> findAllOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findByStatusAndDeletedTrue(String status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.status != 'Inactive' Order By u.createdAt Desc")
    Page<User> findPendingAndActiveUser(Pageable pageable);


}
