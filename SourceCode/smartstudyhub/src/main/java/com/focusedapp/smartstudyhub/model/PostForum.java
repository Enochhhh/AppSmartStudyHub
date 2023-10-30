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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "post_forum")
public class PostForum implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "category_forum_id", nullable = false)
	private CategoryForum categoryForum;
	
	@Column(name = "title", length = 50)
	private String title;
	
	@Column(name = "content", length = 3000)
	private String content;
	
	private String tag;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "total_like")
	private Integer totalLike;
	
	@Column(name = "total_view")
	private Integer totalView;
	
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate; 
	
	@Column(name = "url_post")
	private String urlPost;
	
	@Column(name = "total_type_react")
	private String totalTypeReact;
	
	private String status;
	
	private String limits;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<LikePost> usersLiked;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<CommentPost> usersCommented;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<ReportPost> reportsOfUser;
	
}
