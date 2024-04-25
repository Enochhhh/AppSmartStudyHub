package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.projectioninterface.RankUsersProjectionInterface;

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
	
	List<User> findByRoleNot(String role, Pageable pageable);
	
	List<User> findByRoleNot(String role);
	
	List<User> findByStatusAndRoleNot(String status, String role, Pageable pageable);
	
	List<User> findByStatusAndRoleNot(String status, String role, Sort sort);
	
	Integer countByStatusAndRoleNot(String status, String role);
	
	@Query(value = "select ROW_NUMBER() OVER (ORDER BY SUM(p.time_of_pomodoro) DESC) as ranks, "
			+ "u.id id, u.first_name firstName, u.last_name lastName, u.image_url imageUrl ,sum(p.time_of_pomodoro) totalTimeFocus " + 
			"from users u left join pomodoros p on p.user_id = u.id and p.created_date >= :date and p.is_end_pomo = false " +
			"where u.status = 'ACTIVE' and u.roles != 'ADMIN' " +
			"group by u.id ", nativeQuery = true)
	List<RankUsersProjectionInterface> rankByTimeFocusPreviousMonth(@Param("date") Date date, Pageable pageable);
	
}
