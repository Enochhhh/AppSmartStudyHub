package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Theme;
import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface ThemeDAO extends JpaRepository<Theme, Integer> {

	List<Theme> findByUserIdIsNullAndStatus(String status);
	
	@Query(value = "SELECT * FROM theme t WHERE (t.user_id = :userId or t.user_id IS NULL)"
			+ " AND t.status = :status", nativeQuery = true)
	List<Theme> findByUserIdOrUserIdIsNullAndStatus(@Param("userId") Integer userId, @Param("status") String status);
	
	List<Theme> findByUserIdAndStatus(Integer userId, String status);
	
	Optional<Theme> findByIdAndStatus(Integer themeId, String status);
	
	void deleteByUser(User user);
}
