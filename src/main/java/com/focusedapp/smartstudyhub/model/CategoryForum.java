package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
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
@Table(name = "category_forum")
public class CategoryForum implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name_category", length = 50)
	private String nameCategory;
	
	@Column(name = "total_post")
	private Integer totalPost;
	
	@Column(name = "total_comment")
	private Integer totalComment;
	
	@OneToMany(mappedBy = "categoryForum", fetch = FetchType.LAZY)
	List<PostForum> posts;
	
}
