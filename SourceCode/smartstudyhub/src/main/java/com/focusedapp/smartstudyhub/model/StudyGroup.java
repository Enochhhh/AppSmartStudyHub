package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "study_group")
public class StudyGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "codes")
	private String code;
	
	@Column(name = "name_group", length = 50)
	private String nameGroup;
	
	@Column(name = "descriptions", length = 300)
	private String description;
	
	@Column(name = "total_member")
	private Integer totalMember;
	
	@Column(name = "total_time_focus")
	private Integer totalTimeFocus;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private String status;
	
	@OneToMany(mappedBy = "studyGroup", fetch = FetchType.LAZY)
	private List<UsersJoiningStudyGroup> usersJoiningStudyGroups;
	
}
