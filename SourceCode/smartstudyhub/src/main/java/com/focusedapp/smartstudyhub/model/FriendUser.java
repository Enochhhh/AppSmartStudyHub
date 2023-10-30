package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;

import com.focusedapp.smartstudyhub.model.compositekey.FriendUserKey;

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
@Entity
@Table(name = "friend_users")
public class FriendUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private FriendUserKey id;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@MapsId("userIdFriend")
	@JoinColumn(name = "user_id_friend", nullable = false)
	private User userFriend;
	
	@Column(name = "created_date")
	private Date createdDate;
	
}
