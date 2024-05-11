package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "users")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "user_name", length = 50)
	private String userName;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "passwords")
	private String password;
	
	@Column(name = "phone_number", length = 11)
	private String phoneNumber;
	
	@Column(name = "first_name", length = 50)
	private String firstName;
	
	@Column(name = "last_name", length = 50)
	private String lastName;
	
	@Column(name = "address", length = 100)
	private String address;
	
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	
	@Column(name = "country", length = 50)
	private String country;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "roles", length = 20)
	private String role;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "total_time_focus")
	private Integer totalTimeFocus;
	
	@Column(name = "status")
	private String status;
	
	private String provider;
	
	@Column(name = "time_admin_modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeAdminModified;
	
	@Column(name = "is_two_factor")
	private Boolean isTwoFactor;
	
	@Column(name = "total_works")
	private Integer totalWorks;
	
	@Column(name = "total_pomodoros")
	private Integer totalPomodoros;
	
	@Column(name = "total_works_today")
	private Integer totalWorksToday;
	
	@Column(name = "total_pomodoros_today")
	private Integer totalPomodorosToday;
	
	@Column(name = "total_time_focus_today")
	private Integer totalTimeFocusToday;
	
	@Column(name = "total_works_weekly")
	private Integer totalWorksWeekly;
	
	@Column(name = "total_pomodoros_weekly")
	private Integer totalPomodorosWeekly;
	
	@Column(name = "total_time_focus_weekly")
	private Integer totalTimeFocusWeekly;
	
	@Column(name = "time_last_use")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeLastUse;
	
	@Column(name = "due_date_premium")
	private Date dueDatePremium;
	
	@Column(name = "cover_image")
	private String coverImage;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Report> reports;
	
	@OneToMany(mappedBy = "userWasReported", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Report> reportsFromOtherUser;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<UsersJoiningStudyGroup> groupsUserJoining;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Theme> themes;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Folder> folders;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Project> projects;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Work> works;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Tag> tags;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Pomodoro> pomodoros;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Files> files;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SoundConcentration> soundConcentrations;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SoundDone> soundDones;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<EventSchedule> eventSchedules;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HistoryDaily> historyDailies;
	
}
