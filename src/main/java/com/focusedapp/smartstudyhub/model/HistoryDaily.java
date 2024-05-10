package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "history_daily")
public class HistoryDaily implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "dates")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dates;
	
	@Column(name = "work_ids")
	private String workIds;
	
	@Column(name = "pomodoro_ids")
	private String pomodoroIds;
	
	@Column(name = "total_works_done")
	private Integer totalWorksDone;
	
	@Column(name = "total_pomodoros_done")
	private Integer totalPomodorosDone;
	
	@Column(name = "total_time_focus")
	private Integer totalTimeFocus;
	
}
