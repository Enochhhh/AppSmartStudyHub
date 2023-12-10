package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.compositekey.ManageUserKey;
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
@Table(name = "manage_users")
public class ManageUser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ManageUserKey id;
	
	@ManyToOne
	@MapsId("userManagerId")
	@JoinColumn(name = "user_manager_id", nullable = false)
	private User userManager;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "created_date")
	private Date createdDate;

}
