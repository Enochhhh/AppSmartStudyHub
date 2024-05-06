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
@Builder
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "report")
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "phone_number", length = 11)
	private String phoneNumber;
	
	@Column(name = "title", length = 300)
	private String title;
	
	@Column(name = "where_problem_occur", length = 1000)
	private String whereProblemOccur;
	
	@Column(name = "description_detail", length = 1000)
	private String descriptionDetail;
	
	@Column(name = "what_help", length = 1000)
	private String whatHelp;
	
	@Column(name = "how_problem_affect", length = 1000)
	private String howProblemAffect;
	
	@Column(name = "thing_most_satisfy", length = 1000)
	private String thingMostSatisfy;
	
	@Column(name = "thing_to_improve", length = 1000)
	private String thingToImprove;
	
	@Column(name = "status_report")
	private String statusReport;
	
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "type_report", length = 50)
	private String typeReport;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "url_file")
	private String urlFile;
	
	private String status;
	
}
