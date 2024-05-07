package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import com.focusedapp.smartstudyhub.dao.ReportDAO;
import com.focusedapp.smartstudyhub.model.Report;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.ReportDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusReport;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class ReportService {

	@Autowired
	private ReportDAO reportDAO;
	@Autowired
	private UserService userService;
	
	/**
	 * Save Report to Database
	 * 
	 * @param report
	 * @return
	 */
	public Report persistent(Report report) {
		return reportDAO.save(report);
	}
	
	/**
	 * Create Report
	 * 
	 * @param request
	 * @param userId
	 * @return
	 */
	public ReportDTO create(ReportDTO request, Integer userId) {
		
		User user = userService.findById(userId);
		
		Report report = Report.builder()
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.title(request.getTitle())
				.whereProblemOccur(request.getWhereProblemOccur())
				.descriptionDetail(request.getDescriptionDetail())
				.whatHelp(request.getWhatHelp())
				.howProblemAffect(request.getHowProblemAffect())
				.thingMostSatisfy(request.getThingMostSatisfy())
				.thingToImprove(request.getThingToImprove())
				.statusReport(EnumStatusReport.NOT_SEEN.getValue())
				.createdDate(new Date())
				.typeReport(request.getTypeReport())
				.user(user)
				.urlFile(request.getUrlFile())
				.status(EnumStatus.ACTIVE.getValue())
				.build();
		reportDAO.save(report);
		
		return new ReportDTO(report);
	}
	
	/**
	 * Get Reports by UserId
	 * 
	 * @param userId
	 * @return
	 */
	public List<ReportDTO> getByUserId(Integer userId) {
		List<Report> reports = reportDAO.findByUserIdOrderByCreatedDateDesc(userId);
		if (!CollectionUtils.isEmpty(reports)) {
			return reports.stream()
					.map(r -> new ReportDTO(r))
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	/**
	 * Get Detail Report by id
	 * 
	 * @param id
	 * @return
	 */
	public ReportDTO getById(Integer id) {
		Optional<Report> reportOpt = reportDAO.findById(id);
		if (reportOpt.isEmpty()) {
			return null;
		}
		return new ReportDTO(reportOpt.get());
	}
	
	/**
	 * Get Reports for Admin
	 * 
	 * @return
	 */
	public List<ReportDTO> getAllReportsForAdmin(Pageable pageable) {
		List<Report> reports = reportDAO.findAllReports(pageable);
		if (CollectionUtils.isEmpty(reports)) {
			return new ArrayList<>();
		}
		return reports.stream()
				.map(r -> new ReportDTO(r))
				.collect(Collectors.toList());
	}
	
	/**
	 * Delete report by id
	 * 
	 * @param reportId
	 * @return
	 */
	public Boolean deleteById(Integer reportId) {
		Optional<Report> report = reportDAO.findById(reportId);
		if (report.isEmpty()) {
			return false;
		}
		reportDAO.delete(report.get());
		return true;
	}
	
	/**
	 * Update status report by id
	 * 
	 * @param reportId
	 * @return
	 */
	public Boolean updateStatus(Integer reportId) {
		Optional<Report> report = reportDAO.findById(reportId);
		if (report.isEmpty()) {
			return false;
		}
		report.get().setStatusReport(EnumStatusReport.SEEN.getValue());
		reportDAO.save(report.get());
		return true;
	}
}
