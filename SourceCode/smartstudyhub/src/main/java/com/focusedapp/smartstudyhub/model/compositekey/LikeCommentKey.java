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
public class LikeCommentKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "comment_id", nullable = false)
	private Integer commentId;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
}
