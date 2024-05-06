package com.focusedapp.smartstudyhub.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ReportDAO;
import com.focusedapp.smartstudyhub.model.Report;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.ReportDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusReport;

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
}
