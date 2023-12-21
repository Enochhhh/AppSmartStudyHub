package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

	Optional<User> findByEmailAndProviderAndStatus(String email, String provider, String status);
	
	Boolean existsByEmailAndProvider(String email, String provider);
	
	Optional<User> findTopByOrderByIdDesc();
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByIdAndStatus(Integer id, String status);
	
	User findByUserNameAndProvider(String userName, String provider);
	
	Optional<User> findByEmailAndProvider(String email, String provider);
	
	@Query(value = "SELECT * FROM users u " + 
			"WHERE u.email = :email " + 
				"AND u.provider = :provider " +
				"AND u.status != 'ACTIVE'", nativeQuery = true)
	Optional<User> findByEmailAndProviderAndStatusNotActive(@Param("email") String email, @Param("provider") String prodiver);
	
	Optional<User> findByUserNameAndStatus(String userName, String status);
	
	Optional<User> findByUserName(String userName);
	
	Boolean existsByUserName(String userName);
	
	List<User> findByStatus(String status);
	
}
