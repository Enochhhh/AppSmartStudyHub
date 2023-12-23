package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface TagDAO extends JpaRepository<Tag, Integer> {

	@Query(value = "select * from tag t where t.id in :ids", nativeQuery = true)
	List<Tag> findByIds(@Param("ids") List<Integer> tagIds);
	
	List<Tag> findByUserIdAndStatus(Integer userId, String status);
	
	List<Tag> findByTagNameContainingAndUserIdAndStatus(String keySearch, Integer userId, String status);
	
	void deleteByUser(User user);
}
