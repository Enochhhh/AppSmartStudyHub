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
public class LikePostKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "post_id", nullable = false)
	private Integer postId;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;

}
