package com.pitang.systemusercar.repository;

import com.pitang.systemusercar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

	User findByLogin(String login);

	User findByEmail(String email);
}
