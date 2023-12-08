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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "pomodoros")
public class Pomodoro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "work_id")
	private Work work;

	@ManyToOne
	@JoinColumn(name = "extra_work_id")
	private ExtraWork extraWork;
	
	@Column(name = "pomodoro_name")
	private String pomodoroName;
	
	@Column(name = "time_of_pomodoro")
	private Integer timeOfPomodoro;
	
	@Column(name = "start_time")
	private Date startTime;
	
	@Column(name = "end_time")
	private Date endTime;
	
	@Column(name = "is_start_pomo")
	private Boolean isStartPomo;
	
	@Column(name = "is_end_pomo")
	private Boolean isEndPomo;
	
	private String mode;
	
	@Column(name = "number_pomo_done_of_work")
	private Integer numberPomoDoneOfWork;

}
