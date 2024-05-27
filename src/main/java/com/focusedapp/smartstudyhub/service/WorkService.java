package com.focusedapp.smartstudyhub.service;



import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focusedapp.smartstudyhub.dao.ProjectDAO;
import com.focusedapp.smartstudyhub.dao.WorkDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.DeviceUser;
import com.focusedapp.smartstudyhub.model.ExtraWork;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.model.custom.TagDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkGroupByDateDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkResponseDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkSortedResponseDTO;
import com.focusedapp.smartstudyhub.model.fcm.NotificationMessage;
import com.focusedapp.smartstudyhub.service.firebase.FirebaseMessagingService;
import com.focusedapp.smartstudyhub.util.DateUtils;
import com.focusedapp.smartstudyhub.util.comparator.SortByPriorityComparator;
import com.focusedapp.smartstudyhub.util.comparator.SortByProjectNameComparator;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPriority;
import com.focusedapp.smartstudyhub.util.enumerate.EnumSortType;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusWork;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeRepeat;
import com.focusedapp.smartstudyhub.util.enumerate.EnumUnitRepeat;
import com.focusedapp.smartstudyhub.util.enumerate.EnumZoneId;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class WorkService {
	
	@Autowired
	WorkDAO workDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PomodoroService pomodoroService;
	
	@Autowired
	ExtraWorkService extraWorkService;
	
	@Autowired
	TagService tagService;
	
	@Autowired 
	ThreadService threadService;
	
	@Autowired
	FirebaseMessagingService firebaseMessagingService;
	
	@Autowired
	DeviceService deviceService;
	
	/**
	 * Find Work By Id and Status
	 * 
	 * @param workId
	 * @param status
	 * @return
	 */
	public Work findByIdAndStatus(Integer workId, String status) {
		return workDAO.findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not Found The Work By id and status", "WorkService -> findByIdAndStatus"));
	}
	
	/**
	 * Find Work by Id
	 * 
	 * @return
	 */
	public Work findById(Integer id) {
		return workDAO.findById(id)
				.orElseThrow(() -> new NotFoundValueException("Not Found Work by Id!", "WorkService -> findById"));
	}
	
	/**
	 * Create Work
	 * 
	 * @param dataCreate
	 * @return
	 */
	public WorkDTO createWork(WorkDTO dataCreate) {

		Optional<Project> projectOtp = projectDAO.findByIdAndStatus(dataCreate.getProjectId(), EnumStatus.ACTIVE.getValue());
		Project project = null;
		if (!projectOtp.isEmpty()) {
			project = projectOtp.get();
		}
		
		List<Integer> tagIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dataCreate.getTags())) {
			tagIds = dataCreate.getTags().stream()
						.map(t -> t.getId())
						.collect(Collectors.toList());
		}
		
		List<Tag> tags = tagService.findByIds(tagIds);
		Boolean isRemindered = dataCreate.getTimeWillAnnounce() == null 
				|| (dataCreate.getTimeWillAnnounce() != null 
				&& dataCreate.getTimeWillAnnounce() <= new Date().getTime()) ? false : true;
		Work work = Work.builder()
				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
				.project(project)
				.workName(dataCreate.getWorkName())
				.dueDate(dataCreate.getDueDate() == null ? null : new Date(dataCreate.getDueDate()))
				.priority(dataCreate.getPriority() == null ? EnumPriority.NONE.getValue() : dataCreate.getPriority())
				.numberOfPomodoros(dataCreate.getNumberOfPomodoros() == null ? 0 : dataCreate.getNumberOfPomodoros())
				.timeOfPomodoro(dataCreate.getTimeOfPomodoro() == null ? 25 : dataCreate.getTimeOfPomodoro())
				.isRemindered(isRemindered)
				.numberOfPomodorosDone(0)
				.timePassed(0)
				.tags(tags)
				.createdDate(new Date())
				.timeWillAnnounce(dataCreate.getTimeWillAnnounce() == null ? null : new Date(dataCreate.getTimeWillAnnounce()))
				.typeRepeat(dataCreate.getTypeRepeat())
				.unitRepeat(dataCreate.getUnitRepeat())
				.amountRepeat(dataCreate.getAmountRepeat())
				.daysOfWeekRepeat(dataCreate.getDaysOfWeekRepeat())
				.status(EnumStatus.ACTIVE.getValue())
				.note(dataCreate.getNote())
				.dateEndRepeat(dataCreate.getDateEndRepeat() == null ? null : new Date(dataCreate.getDateEndRepeat()))
				.build();
				
		work = workDAO.save(work);

		return new WorkDTO(work);
	}
	
	/**
	 * Update Work
	 * 
	 * @param dataUpdate
	 * @return
	 */
	public WorkDTO updateWork(WorkDTO dataUpdate) {

		Work workDb = workDAO.findById(dataUpdate.getId())
				.orElseThrow();
		Project project = null;
		if (dataUpdate.getProjectId() != null) {
			Optional<Project> projectOtp = projectDAO.findById(dataUpdate.getProjectId());

			if (!projectOtp.isEmpty()) {
				project = projectOtp.get();
			}
		}
		
		List<TagDTO> tagsReq = dataUpdate.getTags();
		List<Integer> tagIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(tagsReq)) {
			tagIds = tagsReq.stream()
						.map(t -> t.getId())
						.collect(Collectors.toList());
		}
		Boolean isRemindered = dataUpdate.getTimeWillAnnounce() == null 
				|| (dataUpdate.getTimeWillAnnounce() != null 
				&& dataUpdate.getTimeWillAnnounce() <= new Date().getTime()) ? false : true;
		workDb.setProject(project);
		workDb.setWorkName(dataUpdate.getWorkName());
		workDb.setDueDate(dataUpdate.getDueDate() == null ? null : new Date(dataUpdate.getDueDate()));
		workDb.setTimeWillAnnounce(dataUpdate.getTimeWillAnnounce() == null ? null : new Date(dataUpdate.getTimeWillAnnounce()));
		workDb.setPriority(dataUpdate.getPriority());
		workDb.setNumberOfPomodoros(dataUpdate.getNumberOfPomodoros());
		workDb.setTimeOfPomodoro(dataUpdate.getTimeOfPomodoro());
		workDb.setIsRemindered(isRemindered);
		workDb.setNote(dataUpdate.getNote());	
		// workDb.setNumberOfPomodorosDone(dataUpdate.getNumberOfPomodorosDone());
		if (dataUpdate.getStartTime() != null) {
			workDb.setStartTime(new Date(dataUpdate.getStartTime()));
		}
		if (dataUpdate.getEndTime() != null) {
			workDb.setEndTime(new Date(dataUpdate.getEndTime()));
		}		
		workDb.setTags(tagService.findByIds(tagIds));
		workDb.setStatus(dataUpdate.getStatus() == null ? EnumStatus.ACTIVE.getValue() : dataUpdate.getStatus());
		if (dataUpdate.getTimeWillAnnounce() != null) {
			workDb.setTimeWillAnnounce(new Date(dataUpdate.getTimeWillAnnounce()));
		}
		workDb.setTypeRepeat(dataUpdate.getTypeRepeat());
		workDb.setUnitRepeat(dataUpdate.getUnitRepeat());
		workDb.setAmountRepeat(dataUpdate.getAmountRepeat());
		workDb.setDaysOfWeekRepeat(dataUpdate.getDaysOfWeekRepeat());
		if (dataUpdate.getDateEndRepeat() != null) {
			workDb.setDateEndRepeat(new Date(dataUpdate.getDateEndRepeat()));
		}
				
		workDb = workDAO.save(workDb);

		return new WorkDTO(workDb);
	}
	
	/**
	 * Mark Deleted Work
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO markDeletedWork(Integer workId) {

		Optional<Work> workOpt = workDAO.findById(workId);
		
		if (workOpt.isEmpty()) {
			return null;
		}
		Work workDb = workOpt.get();
		
		List<ExtraWork> extraWorks = workDb.getExtraWorks();
		if (extraWorks != null) {
			extraWorks.stream()
				.forEach(ew -> {
					extraWorkService.markDeleted(ew.getId());
				});
		}
		if (!workDb.getStatus().equals(EnumStatus.DELETED.getValue())) {
			workDb.setOldStatus(workDb.getStatus());
			workDb.setStatus(EnumStatus.DELETED.getValue());
		}	
				
		workDb = workDAO.save(workDb);

		return new WorkDTO(workDb);
	}
	
	/**
	 * Delete Completely Work By Id
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO deleteById(Integer workId) {
		
		Work workDb = findById(workId);
		workDAO.delete(workDb);
		
		return new WorkDTO(workDb);
	}
	
	/**
	 * Get works by Project And Status
	 * 
	 * @param projectId
	 * @param status
	 * @return
	 */
	public List<WorkDTO> getWorksByProjectAndStatus(Integer projectId, Integer userId, String status) {
		
		List<Work> works = workDAO.findByProjectIdAndUserIdAndStatus(projectId, userId, status);
		
		return works.stream()
				.map(w -> new WorkDTO(w))
				.collect(Collectors.toList());
	}
	
	/**
	 * Get Detail Work
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO getDetailWork(Integer workId) {
		Work work = findById(workId);
		
		return new WorkDTO(work);
	}
	
	/**
	 * Update Work
	 * 
	 * @param dataUpdate
	 * @return
	 */
	public WorkDTO markCompleted(Integer workId) {

		Optional<Work> workOpt = workDAO.findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue());
		if (workOpt.isEmpty()) {
			return null;
		}
		Work workDb = workOpt.get();
		
		List<ExtraWork> extraWorks = workDb.getExtraWorks();
		if (extraWorks != null) {
			extraWorks.stream()
				.forEach(ew -> {
					if (ew.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
						extraWorkService.markCompleted(ew.getId());
					}
				});
		}
		workDb.setStatus(EnumStatus.COMPLETED.getValue());
		workDb.setEndTime(new Date());
		if (workDb.getStartTime() == null) {
			workDb.setStartTime(new Date());
		}
		workDb.setDateMarkCompleted(new Date());
		
		// Set information repeat to null, it will ensure when uncomplete this work. This work won't be repeated
		workDb.setTypeRepeat(null);
		workDb.setUnitRepeat(null);
		workDb.setAmountRepeat(null);
		workDb.setDaysOfWeekRepeat(null);
		workDb.setDateEndRepeat(null);
		
		PomodoroDTO pomodoroRequest = PomodoroDTO.builder()
				.userId(workDb.getUser().getId())
				.workId(workDb.getId())
				.endTime(new Date().getTime())
				.isEndPomo(true)
				.build();
		User user = workDb.getUser();
		Integer totalWorks = user.getTotalWorks() == null ? 0 : user.getTotalWorks();
		Integer totalWorksToday = user.getTotalWorksToday() == null ? 0 : user.getTotalWorksToday();
		Integer totalWorksWeekly = user.getTotalWorksWeekly() == null ? 0 : user.getTotalWorksWeekly();
		user.setTotalWorks(totalWorks + 1);
		user.setTotalWorksToday(totalWorksToday + 1);
		user.setTotalWorksWeekly(totalWorksWeekly + 1);
		workDb.setUser(user);
		workDb = workDAO.save(workDb);
		pomodoroService.createPomodoro(pomodoroRequest);

		return new WorkDTO(workDb);
	}
	
	/**
	 * Recover Work
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO recover(Integer workId) {
		Optional<Work> workOpt = workDAO.findById(workId);
		if (workOpt.isEmpty()) {
			return null;
		}
		Work work = workOpt.get();
		
		if (work.getStatus().equals(EnumStatus.COMPLETED.getValue())) {
			work.setEndTime(null);
			work.setStatus(EnumStatus.ACTIVE.getValue());
			User user = work.getUser();
			
			Date nowDate = new Date();
			Date startDayOfWeek = DateUtils.getAnyDayOfWeekFromDateSpecified(nowDate, 2, 
					ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()));
			startDayOfWeek = DateUtils.setTimeOfDateToMidnight(startDayOfWeek.getTime());
			Date endDayOfWeek = DateUtils.getAnyDayOfWeekFromDateSpecified(nowDate, 8, 
					ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()));
			endDayOfWeek = DateUtils.addDaysForDate(endDayOfWeek, 1);
			endDayOfWeek = DateUtils.setTimeOfDateToMidnight(endDayOfWeek.getTime());
			Integer totalWorks = user.getTotalWorks() == null ? 0 : user.getTotalWorks() - 1;
			if (totalWorks < 0) { totalWorks = 0; }
			user.setTotalWorks(totalWorks);
			if (DateUtils.setTimeOfDateToMidnight(work.getDateMarkCompleted().getTime()).getTime() 
					== DateUtils.setTimeOfDateToMidnight(nowDate.getTime()).getTime()) {
				Integer totalWorksToday = user.getTotalWorksToday() == null ? 0 : user.getTotalWorksToday() - 1;
				if (totalWorksToday < 0) { totalWorksToday = 0; }
				Integer totalWorksWeekly = user.getTotalWorksWeekly() == null ? 0 : user.getTotalWorksWeekly() - 1;
				if (totalWorksWeekly < 0) { totalWorksWeekly = 0; }
				user.setTotalWorksToday(totalWorksToday);
				user.setTotalWorksWeekly(totalWorksWeekly);
			} else if (work.getDateMarkCompleted().getTime() >= startDayOfWeek.getTime() 
					&& work.getDateMarkCompleted().getTime() < endDayOfWeek.getTime()) {
				Integer totalWorksWeekly = user.getTotalWorksWeekly() == null ? 0 : user.getTotalWorksWeekly() - 1;
				if (totalWorksWeekly < 0) { totalWorksWeekly = 0; }
				user.setTotalWorksWeekly(totalWorksWeekly);
			}
		
			work.setUser(user);
			
			List<ExtraWork> extraWorks = work.getExtraWorks() == null ? new ArrayList<>() : work.getExtraWorks();
			extraWorks.stream()
				.forEach(ew -> {
					if (ew.getStatus().equals(EnumStatus.COMPLETED.getValue())) {
						extraWorkService.recover(ew.getId());
					}
				});					
		} else if (work.getStatus().equals(EnumStatus.DELETED.getValue())) {
			work.setStatus(work.getOldStatus() == null ? EnumStatus.ACTIVE.getValue() : work.getOldStatus());
			work.setOldStatus(null);
			if (work.getProject() != null && work.getProject().getStatus().equals(EnumStatus.DELETED.getValue())) {
				work.setProject(null);
			}
			List<ExtraWork> extraWorks = work.getExtraWorks() == null ? new ArrayList<>() : work.getExtraWorks();
			extraWorks.stream()
				.forEach(ew -> {
					if (ew.getStatus().equals(EnumStatus.DELETED.getValue())) {
						extraWorkService.recover(ew.getId());
					}
				});
		}
		
		work = workDAO.save(work);
		
		return new WorkDTO(work);
	}	
	
	/**
	 * Searh Work By Name
	 * 
	 * @param keySearch
	 * @return
	 */
	public List<WorkDTO> searchByName(String keySearch, Integer userId) {
		List<Work> works = new ArrayList<>();
		if (StringUtils.isEmpty(keySearch)) {
			works = workDAO.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		} else {
			works = workDAO.findByWorkNameContainingAndUserIdAndStatusBetween(keySearch, userId, EnumStatus.ACTIVE.getValue(), 
					EnumStatus.COMPLETED.getValue());
		}
		return works.stream()
					.map(w -> new WorkDTO(w))
					.collect(Collectors.toList());
	}
	
	/**
	 * Get Works by type
	 * 
	 * @param type
	 * @return
	 */
	public WorkResponseDTO getWorkByType(String type, Integer userId) {
		
		List<Work> listWorkActive = workDAO.findByUserIdAndOneOfTwoStatus(userId, EnumStatus.ACTIVE.getValue(), EnumStatus.COMPLETED.getValue());
		if (CollectionUtils.isEmpty(listWorkActive)) {
			return new WorkResponseDTO(new ArrayList<>());
		}
		List<WorkDTO> worksConvert = listWorkActive.stream()
										.map(w -> new WorkDTO(w))
										.collect(Collectors.toList());		
		Comparator<WorkDTO> comparator = Comparator.comparing(WorkDTO::getCreatedDate,
                Comparator.nullsFirst(Comparator.naturalOrder()));
		Date nowDate = new Date();
		// Convert to LocalDate Timezone VietNam to get DayOfWeek Exactly
		LocalDateTime dateTimeZone = DateUtils.convertoToLocalDateTime(nowDate);
		// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
		int dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
		if (type.equals(EnumStatusWork.TODAY.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.TODAY.getValue()) 
							|| w.getStatusWork().equals(EnumStatusWork.OUTOFDATE.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.OUTOFDATE.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.OUTOFDATE.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.TOMORROW.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.TOMORROW.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.THISWEEK.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.THISWEEK.getValue())
							|| w.getStatusWork().equals(EnumStatusWork.TODAY.getValue())
							|| (w.getStatusWork().equals(EnumStatusWork.TOMORROW.getValue()) && (dayOfWeek + 1) <= 8))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.NEXT7DAY.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.NEXT7DAY.getValue())
							|| w.getStatusWork().equals(EnumStatusWork.TODAY.getValue())
							|| w.getStatusWork().equals(EnumStatusWork.TOMORROW.getValue())
							|| w.getStatusWork().equals(EnumStatusWork.THISWEEK.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.SOMEDAY.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.SOMEDAY.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.ALL.getValue())) {
			worksConvert = worksConvert.stream()
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.TASK_DEFAULT.getValue())){
			worksConvert = worksConvert.stream()
					.filter(w -> w.getProjectId() == null)
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.PLANNED.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> !w.getStatusWork().equals(EnumStatusWork.OUTOFDATE.getValue()) 
							&& !w.getStatusWork().equals(EnumStatusWork.SOMEDAY.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		}
		return new WorkResponseDTO(worksConvert);
	}
	
	/**
	 * Get Works by priority
	 * 
	 * @param priority
	 * @param userId
	 * @return
	 */
	public WorkResponseDTO getByPriority(String priority, Integer userId) {
		
		List<Work> listWorks = workDAO.findByUserIdAndPriorityAndOneOfTwoStatus(userId, priority, EnumStatus.ACTIVE.getValue(), 
				EnumStatus.COMPLETED.getValue());
		
		if (CollectionUtils.isEmpty(listWorks)) {
			return new WorkResponseDTO(new ArrayList<>());
		}
		
		Comparator<WorkDTO> comparator = Comparator.comparing(WorkDTO::getCreatedDate,
				Comparator.nullsFirst(Comparator.naturalOrder()));
		
		List<WorkDTO> worksConvert = listWorks.stream()
				.map(w -> new WorkDTO(w))
				.sorted(comparator.reversed())
				.collect(Collectors.toList());		
		
		return new WorkResponseDTO(worksConvert);
				
	}
	
	/**
	 * Get Work Completed of User
	 * 
	 * @param userId
	 * @return
	 */
	public List<WorkGroupByDateDTO> getWorkCompletedOfUser(Integer userId) {
		
		List<Work> worksDb = workDAO.findByUserIdAndStatus(userId, EnumStatus.COMPLETED.getValue());
		
		Map<Long, List<WorkDTO>> mapWork = worksDb.stream()	
				.map(work -> new WorkDTO(work))				
				.collect(Collectors.groupingBy(w -> { 					
						return DateUtils.setTimeOfDateToMidnight(w.getEndTime()).getTime();
					}, Collectors.toList()));
		mapWork.values().forEach(list -> list.sort(Comparator.comparing(WorkDTO::getEndTime).reversed()));
		List<WorkGroupByDateDTO> works = new ArrayList<>();
		for (Map.Entry<Long, List<WorkDTO>> entry : mapWork.entrySet()) {
			works.add(new WorkGroupByDateDTO(entry.getKey(), entry.getValue()));
		}
		return works;
	}
	
	/**
	 * Sort Work by type
	 * 
	 * @param type
	 * @param works
	 * @return
	 */
	public List<WorkSortedResponseDTO> sortWork(String type, List<WorkDTO> works) {
		
		Comparator<WorkDTO> comparator = Comparator.comparing(WorkDTO::getCreatedDate,
                Comparator.nullsFirst(Comparator.naturalOrder()));
		
		List<WorkSortedResponseDTO> workSortedList = new ArrayList<>();
		if (type.equals(EnumSortType.PROJECT.getValue())) {
			Map<String, List<WorkDTO>> mapWork = works.stream()	
					.collect(Collectors.groupingBy(w -> {
						if (w.getProjectId() == null) {
							return "Task Default";
						}
						Optional<Project> projectOption = projectDAO.findById(w.getProjectId());
						if (projectOption.isEmpty()) {
							return "Task Default";
						}
						Project project = projectOption.get();
						return project.getProjectName();
					},Collectors.toList()));
			
			mapWork = mapWork.entrySet().stream()
					.sorted(Map.Entry.<String, List<WorkDTO>>comparingByKey(new SortByProjectNameComparator("Task Default")))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
						
			for (Map.Entry<String, List<WorkDTO>> entry : mapWork.entrySet()) {
				List<WorkDTO> worksRes = entry.getValue().stream()
						.sorted(comparator.reversed())
						.collect(Collectors.toList());
				workSortedList.add(new WorkSortedResponseDTO(entry.getKey(), worksRes));
			}			
		} else if (type.equals(EnumSortType.PRIORITY.getValue())) {
			Map<String, List<WorkDTO>> mapWork = works.stream()	
					.collect(Collectors.groupingBy(w -> w.getPriority(),Collectors.toList()));
			
			mapWork = mapWork.entrySet().stream()
					.sorted(Map.Entry.<String, List<WorkDTO>>comparingByKey(new SortByPriorityComparator()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			
			for (Map.Entry<String, List<WorkDTO>> entry : mapWork.entrySet()) {
				List<WorkDTO> worksRes = entry.getValue().stream()
						.sorted(comparator.reversed())
						.collect(Collectors.toList());
				workSortedList.add(new WorkSortedResponseDTO(entry.getKey(), worksRes));
			}
		} else if (type.equals(EnumSortType.DUEDATE.getValue())) {
			List<WorkDTO> worksDueDateNull = new ArrayList<>();
			List<WorkDTO> worksRemain = new ArrayList<>();
			
			for (WorkDTO work : works) {
				if (work.getDueDate() == null) {
					worksDueDateNull.add(work);
				} else {
					worksRemain.add(work);
				}
			}
			
			Map<Long, List<WorkDTO>> mapWork = worksRemain.stream()	
					.collect(Collectors.groupingBy(w -> w.getDueDate(),Collectors.toList()));
			
			mapWork = mapWork.entrySet().stream()
					.sorted(Map.Entry.<Long, List<WorkDTO>>comparingByKey().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			
			for (Map.Entry<Long, List<WorkDTO>> entry : mapWork.entrySet()) {
				List<WorkDTO> worksRes = entry.getValue().stream()
						.sorted(comparator.reversed())
						.collect(Collectors.toList());
				workSortedList.add(new WorkSortedResponseDTO(entry.getKey(), worksRes));
			}
			worksDueDateNull = worksDueDateNull.stream()
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
			workSortedList.add(new WorkSortedResponseDTO(0, worksDueDateNull));
		}
		return workSortedList;
	}
	
	/**
	 * Get Work Deleted of User
	 * 
	 * @param userId
	 * @return
	 */
	public List<WorkDTO> getWorkDeletedOfUser(Integer userId) {
		
		List<Work> works = workDAO.findByUserIdAndStatus(userId, EnumStatus.DELETED.getValue());
		
		return works.stream()
				.map(w -> new WorkDTO(w))
				.collect(Collectors.toList());	
		
	}
	
	/**
	 * Delete By User
	 * 
	 * @param user
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	public void deleteAllWorksOfUser(User user) {
		workDAO.deleteByUser(user);
	}

	/**
	 * Persistent all works in list
	 * 
	 * @param works
	 * @return
	 */
	public List<Work> persistentAll(List<Work> works) {
		return workDAO.saveAll(works);
	}
	
	/**
	 * Repeat Work Service
	 * 
	 * @return
	 */
	public WorkDTO repeat(Integer workId, User user) {
		Date nowDate = new Date();
		Work work = findById(workId);
		
		// Check if now date is the date end repeat
		if (work.getDateEndRepeat() != null && work.getDateEndRepeat().getTime() <= nowDate.getTime()){
			return markCompleted(workId);
		}
		Work newWork = new Work(work);
		
		EnumTypeRepeat enumTypeRepeat = EnumTypeRepeat.getByValue(newWork.getTypeRepeat());
		
		Date timeRepeat = newWork.getDueDate();
		int dayOfWeek = 0;
		LocalDateTime dateTimeZone = null;
		int indexDayOfWeek = 0;
		switch(enumTypeRepeat) {
			case DAILY:
				do {
					timeRepeat = DateUtils.addDaysForDate(timeRepeat, 1);
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				newWork.setDueDate(timeRepeat);
				break;
			case ORDINARY_DAY:		
				do {
					timeRepeat = DateUtils.addDaysForDate(timeRepeat, 1);
					dateTimeZone = DateUtils.convertoToLocalDateTime(timeRepeat);
					// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
					dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1 &&
						dayOfWeek < 7));
				newWork.setDueDate(timeRepeat);
				break;
			case WEEKEND:
				do {
					timeRepeat = DateUtils.addDaysForDate(timeRepeat, 1);
					dateTimeZone = DateUtils.convertoToLocalDateTime(timeRepeat);
					// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
					dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1 &&
						dayOfWeek >= 7));
				newWork.setDueDate(timeRepeat);
				break;
			case WEEKLY:
				do {
					timeRepeat = DateUtils.addWeeksForDate(timeRepeat, 1);
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				newWork.setDueDate(timeRepeat);
				break;
			case ONCE_EVERY_TWO_WEEKS:
				do {
					timeRepeat = DateUtils.addWeeksForDate(timeRepeat, 2);
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				newWork.setDueDate(timeRepeat);
				break;
			case MONTHLY:		
				do {
					timeRepeat = DateUtils.addMonthsForDate(timeRepeat, 1);
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				newWork.setDueDate(timeRepeat);
				break;
			case EVERY_THREE_MONTH:
				do {
					timeRepeat = DateUtils.addMonthsForDate(timeRepeat, 3);
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				newWork.setDueDate(timeRepeat);
				break;
			case EVERY_SIX_MONTH:
				timeRepeat = DateUtils.addMonthsForDate(timeRepeat, 6);
				do {
					timeRepeat = DateUtils.addMonthsForDate(timeRepeat, 6);
				} while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				newWork.setDueDate(timeRepeat);
				break;
			default:
				if (newWork.getUnitRepeat().equals(EnumUnitRepeat.DAY.getValue())) {
					timeRepeat = DateUtils.addDaysForDate(timeRepeat, newWork.getAmountRepeat());
					while (!(timeRepeat.getTime() > nowDate.getTime() &&
							(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
						timeRepeat = DateUtils.addDaysForDate(timeRepeat, newWork.getAmountRepeat());
					}
				} else if (newWork.getUnitRepeat().equals(EnumUnitRepeat.WEEK.getValue())) {
					List<String> daysOfWeekList = new ArrayList<>();
					if (newWork.getDaysOfWeekRepeat() != null) {
						daysOfWeekList = Arrays.asList(newWork.getDaysOfWeekRepeat().split(", "));
					}
					dateTimeZone = DateUtils.convertoToLocalDateTime(timeRepeat);					
					do {
						dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
						indexDayOfWeek = daysOfWeekList.indexOf(String.valueOf(dayOfWeek));
						if (daysOfWeekList.size() == 0) {
							timeRepeat = DateUtils.addWeeksForDate(timeRepeat, newWork.getAmountRepeat());
						} else if (indexDayOfWeek == -1) {
							int index = -1;
							for (int i = 0; i < daysOfWeekList.size(); i++) {
								if (Integer.valueOf(dayOfWeek) < Integer.valueOf(daysOfWeekList.get(i))) {
									index = i;
									break;
								}
							}
							if (index == -1) {
								timeRepeat = DateUtils.addWeeksForDate(timeRepeat, newWork.getAmountRepeat());
								timeRepeat = DateUtils.addDaysForDate(timeRepeat, 
										Integer.valueOf(daysOfWeekList.get(0)) - Integer.valueOf(dayOfWeek));
							} else {
								timeRepeat = DateUtils.addDaysForDate(timeRepeat, 
										Integer.valueOf(daysOfWeekList.get(index)) - Integer.valueOf(dayOfWeek));
							}
						}
						else if (indexDayOfWeek == daysOfWeekList.size() - 1) {
							timeRepeat = DateUtils.addWeeksForDate(timeRepeat, newWork.getAmountRepeat());
							timeRepeat = DateUtils.addDaysForDate(timeRepeat, 
									Integer.valueOf(daysOfWeekList.get(0)) - Integer.valueOf(daysOfWeekList.get(indexDayOfWeek)));
						} else {
							timeRepeat = DateUtils.addDaysForDate(timeRepeat, 
									Integer.valueOf(daysOfWeekList.get(indexDayOfWeek + 1)) - Integer.valueOf(daysOfWeekList.get(indexDayOfWeek)));
						}
					} while(!(timeRepeat.getTime() > nowDate.getTime() &&
							(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				} else if (newWork.getUnitRepeat().equals(EnumUnitRepeat.MONTH.getValue())) {
					do {
						timeRepeat = DateUtils.addMonthsForDate(timeRepeat, newWork.getAmountRepeat());
					} while (!(timeRepeat.getTime() > nowDate.getTime() &&
							(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				} else {
					do {
						timeRepeat = DateUtils.addYearsForDate(timeRepeat, newWork.getAmountRepeat());
					} while (!(timeRepeat.getTime() > nowDate.getTime() &&
							(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1));
				}
				newWork.setDueDate(timeRepeat);
		}
		if (newWork.getDueDate().getTime() > newWork.getDateEndRepeat().getTime()) {
			return markCompleted(workId); 
		}
		workDAO.save(newWork);
		newWork.setDataInRelationship(work);
		markCompleted(workId);
		workDAO.save(newWork);
		return new WorkDTO(newWork);
	}
	
	/**
	 * Find Works by due date between
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Work> findByDueDateBetweenAndStatusNotAndUser(Date startDate, Date endDate, String status, User user) {
		return workDAO.findByDueDateBetweenAndStatusNotAndUser(startDate, endDate, status, user);
	}
	
	/**
	 * Find Works by list id for history activity
	 * 
	 * @param workIds
	 * @return
	 */
	public List<WorkDTO> getByIdInAndStatusForHistoryActivity(List<Integer> workIds, String status) {
		List<Work> works = workDAO.findByIdInAndStatus(workIds, status);
		List<WorkDTO> worksResponse = new ArrayList<>();
		if (CollectionUtils.isEmpty(works)) {
			workIds.stream().forEach(id -> {
				worksResponse.add(new WorkDTO("This work has been deleted !"));
			});
		} else {
			workIds.stream().forEach(id -> {
				Optional<Work> workOpt = works.stream().filter(w -> w.getId().equals(id)).findFirst();
				if (workOpt.isEmpty()) {
					worksResponse.add(new WorkDTO("This work has been deleted !"));
				} else {
					worksResponse.add(new WorkDTO(workOpt.get()));
				}				
			});
		}
		return worksResponse;
	}
	
	/**
	 * Find Work by userid and status and date mark completed
	 * 
	 * @param userId
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Work> findByUserIdAndStatusAndDateMarkCompletedGreaterThanEqualAndDateMarkCompletedLessThan(
			Integer userId, String status, Date startDate, Date endDate) {
		return workDAO.findByUserIdAndStatusAndDateMarkCompletedGreaterThanEqualAndDateMarkCompletedLessThan(userId, 
				status, startDate, endDate);
	}
	
	/** 
	 * Delete All Works of User by thread
	 * 
	 * @param userId
	 */
	public Boolean deleteCompletelyAllWorksOfUser(Integer userId) {
		threadService.deleteAllWorksOfUser(userId);
		return true;
	}
	
	/**
	 * Send Notification Work to device of User if come to time will announce
	 * 
	 */
	public void sendNotificationWorkToDeviceUser() {
		Date nowDate = new Date();
		List<Work> works = workDAO.findByTimeWillAnnounceLessThanEqualAndStatusAndIsReminderedTrue(nowDate, 
				EnumStatus.ACTIVE.getValue());
		works.stream().forEach(w -> {
			List<DeviceUser> deviceUsers = deviceService.findDeviceUserByUserId(w.getUser().getId());
			if (CollectionUtils.isEmpty(deviceUsers)) {
				return;
			}
			List<String> registrationTokens = deviceUsers.stream()
					.map(d -> d.getDevice().getRegistrationToken())
					.collect(Collectors.toList());
			NotificationMessage notificationMessage = NotificationMessage.builder()
					.registrationTokens(registrationTokens)
					.title("Reminder")
					.body(w.getWorkName())
					.image("https://journaldev.nyc3.cdn.digitaloceanspaces.com/2018/01/java-simpledateformat.png")
					.build();
			firebaseMessagingService.sendNotification(notificationMessage);
		});
	}
	
	public void sendNotificationWorkToDeviceUserTest() {
		Date nowDate = new Date();

		List<String> registrationTokens = new ArrayList<>();
		registrationTokens.add("5cf5003014687530291859284fc5959d2ae9f52538c65cbd50f5a03a3ef3ee18");
		NotificationMessage notificationMessage = NotificationMessage.builder().registrationTokens(registrationTokens)
				.title("Reminder").body("Hello Boy")
				.image("https://journaldev.nyc3.cdn.digitaloceanspaces.com/2018/01/java-simpledateformat.png").build();
		firebaseMessagingService.sendNotification(notificationMessage);

	}
	
}