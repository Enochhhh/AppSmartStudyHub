package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.compositekey.LikePostKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "like_post")
public class LikePost implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private LikePostKey id;
	
	@ManyToOne
	@MapsId("postId")
	@JoinColumn(name = "post_id", nullable = false)
	private PostForum post;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "type_react")
	private String typeReact;
	
}
