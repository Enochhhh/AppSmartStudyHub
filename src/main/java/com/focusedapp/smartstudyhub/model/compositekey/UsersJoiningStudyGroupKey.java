package com.focusedapp.smartstudyhub.model.compositekey;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UsersJoiningStudyGroupKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "study_group_id", nullable = false)
	private Integer studyGroupId;
	
}
