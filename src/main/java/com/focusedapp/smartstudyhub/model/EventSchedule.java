package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Builder
@Table(name = "event_schedule")
public class EventSchedule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "event_id_group")
	private Integer eventIdGroup;
	
	@Column(name = "type_event")
	private String typeEvent;
	
	private String location;
	
	@Column(name = "is_all_day")
	private Boolean isAllDay;
	
	@Column(name = "color_code")
	private String colorCode;

	@Column(name = "announce_date")
	private Date announceDate;
	
	private String descriptions;
	
	@Column(name = "url_attach_file")
	private String urlAttachFile;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	private String status;
	
	@ManyToMany
	@JoinTable(
			name = "assignee_event_schedule",
			joinColumns = @JoinColumn(name = "event_id", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "assignee_id", nullable = false))
	List<User> users;
	
}
