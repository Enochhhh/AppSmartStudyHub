package com.focusedapp.smartstudyhub.service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.focusedapp.smartstudyhub.dao.ProjectDAO;
import com.focusedapp.smartstudyhub.dao.WorkDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
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
import com.focusedapp.smartstudyhub.util.MethodUtils;
import com.focusedapp.smartstudyhub.util.comparator.SortByPriorityComparator;
import com.focusedapp.smartstudyhub.util.comparator.SortByProjectNameComparator;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPriority;
import com.focusedapp.smartstudyhub.util.enumerate.EnumSortType;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusWork;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeRepeat;
import com.focusedapp.smartstudyhub.util.enumerate.EnumUnitRepeat;
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
		
		Work work = Work.builder()
				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
				.project(project)
				.workName(dataCreate.getWorkName())
				.dueDate(dataCreate.getDueDate() == null ? null : new Date(dataCreate.getDueDate()))
				.priority(dataCreate.getPriority() == null ? EnumPriority.NONE.getValue() : dataCreate.getPriority())
				.numberOfPomodoros(dataCreate.getNumberOfPomodoros() == null ? 0 : dataCreate.getNumberOfPomodoros())
				.timeOfPomodoro(dataCreate.getTimeOfPomodoro() == null ? 25 : dataCreate.getTimeOfPomodoro())
				.isRemindered(Boolean.FALSE)
				.numberOfPomodorosDone(dataCreate.getNumberOfPomodorosDone() == null ? 0 : dataCreate.getNumberOfPomodorosDone())
				.timePassed(dataCreate.getTimePassed() == null ? 0 : dataCreate.getTimePassed())
				.tags(tags)
				.createdDate(new Date())
				.status(EnumStatus.ACTIVE.getValue())
				.note(dataCreate.getNote())
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
		
		workDb.setProject(project);
		workDb.setWorkName(dataUpdate.getWorkName());
		workDb.setDueDate(dataUpdate.getDueDate() == null ? null : new Date(dataUpdate.getDueDate()));
		workDb.setTimeWillAnnounce(dataUpdate.getTimeWillAnnounce() == null ? null : new Date(dataUpdate.getTimeWillAnnounce()));
		workDb.setPriority(dataUpdate.getPriority());
		workDb.setNumberOfPomodoros(dataUpdate.getNumberOfPomodoros());
		workDb.setTimeOfPomodoro(dataUpdate.getTimeOfPomodoro());
		workDb.setIsRemindered(dataUpdate.getIsRemindered());
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
		
		// Set information repeat to null, it will ensure when uncomplete this work. This work won't be repeated
		workDb.setTypeRepeat(null);
		workDb.setUnitRepeat(null);
		workDb.setAmountRepeat(null);
		workDb.setDaysOfWeekRepeat(null);
		
		PomodoroDTO pomodoroRequest = PomodoroDTO.builder()
				.userId(workDb.getUser().getId())
				.workId(workDb.getId())
				.endTime(new Date().getTime())
				.isEndPomo(true)
				.build();
		User user = workDb.getUser();
		Integer totalWorks = user.getTotalWorks() == null ? 0 : user.getTotalWorks();
		user.setTotalWorks(totalWorks + 1);
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
			Integer totalWorks = user.getTotalWorks() == null ? 0 : user.getTotalWorks() - 1;
			if (totalWorks < 0) { totalWorks = 0; }
			user.setTotalWorks(totalWorks);
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
		LocalDateTime dateTimeZone = MethodUtils.convertoToLocalDateTime(nowDate);
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
						Date date = new Date(w.getEndTime());
						// Get date at time 00:00:00
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
				        calendar.set(Calendar.SECOND, 0);
				        calendar.set(Calendar.MILLISECOND, 0);
				        date = calendar.getTime();
						return date.getTime();
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
		Work work = findById(workId);
		Work newWork = new Work(work);
		workDAO.save(newWork);
		newWork.setDataInRelationship(work);
		markCompleted(workId);
		EnumTypeRepeat enumTypeRepeat = EnumTypeRepeat.getByValue(newWork.getTypeRepeat());
		
		Date timeRepeat = newWork.getDueDate();
		Date nowDate = new Date();
		int dayOfWeek = 0;
		LocalDateTime dateTimeZone = null;
		switch(enumTypeRepeat) {
			case DAILY:
				timeRepeat = MethodUtils.addDaysForDate(timeRepeat, 1);
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
					timeRepeat = MethodUtils.addDaysForDate(timeRepeat, 1);
				}
				newWork.setDueDate(timeRepeat);
				break;
			case ORDINARY_DAY:
				timeRepeat = MethodUtils.addDaysForDate(timeRepeat, 1);
				dateTimeZone = MethodUtils.convertoToLocalDateTime(timeRepeat);
				// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
				dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1 &&
						dayOfWeek < 7)) {
					timeRepeat = MethodUtils.addDaysForDate(timeRepeat, 1);
					dateTimeZone = MethodUtils.convertoToLocalDateTime(timeRepeat);
					// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
					dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
				}
				newWork.setDueDate(timeRepeat);
				break;
			case WEEKEND:
				timeRepeat = MethodUtils.addDaysForDate(timeRepeat, 1);
				dateTimeZone = MethodUtils.convertoToLocalDateTime(timeRepeat);
				// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
				dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1 &&
						dayOfWeek >= 7)) {
					timeRepeat = MethodUtils.addDaysForDate(timeRepeat, 1);
					dateTimeZone = MethodUtils.convertoToLocalDateTime(timeRepeat);
					// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
					dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
				}
				newWork.setDueDate(timeRepeat);
				break;
			case WEEKLY:
				timeRepeat = MethodUtils.addWeeksForDate(timeRepeat, 1);
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
					timeRepeat = MethodUtils.addWeeksForDate(timeRepeat, 1);
				}
				newWork.setDueDate(timeRepeat);
				break;
			case ONCE_EVERY_TWO_WEEKS:
				timeRepeat = MethodUtils.addWeeksForDate(timeRepeat, 2);
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
					timeRepeat = MethodUtils.addWeeksForDate(timeRepeat, 2);
				}
				newWork.setDueDate(timeRepeat);
				break;
			case MONTHLY:
				timeRepeat = MethodUtils.addMonthsForDate(timeRepeat, 1);
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
					timeRepeat = MethodUtils.addMonthsForDate(timeRepeat, 1);
				}
				newWork.setDueDate(timeRepeat);
				break;
			case EVERY_THREE_MONTH:
				timeRepeat = MethodUtils.addMonthsForDate(timeRepeat, 3);
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
					timeRepeat = MethodUtils.addMonthsForDate(timeRepeat, 3);
				}
				newWork.setDueDate(timeRepeat);
				break;
			case EVERY_SIX_MONTH:
				timeRepeat = MethodUtils.addMonthsForDate(timeRepeat, 6);
				while (!(timeRepeat.getTime() > nowDate.getTime() &&
						(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
					timeRepeat = MethodUtils.addMonthsForDate(timeRepeat, 6);
				}
				newWork.setDueDate(timeRepeat);
				break;
			default:
				if (newWork.getUnitRepeat().equals(EnumUnitRepeat.DAY.getValue())) {
					timeRepeat = MethodUtils.addDaysForDate(timeRepeat, newWork.getAmountRepeat());
					while (!(timeRepeat.getTime() > nowDate.getTime() &&
							(timeRepeat.getTime() - nowDate.getTime()) / (60 * 60 * 1000) >= 1)) {
						timeRepeat = MethodUtils.addDaysForDate(timeRepeat, newWork.getAmountRepeat());
					}
					newWork.setDueDate(timeRepeat);
				} else if (newWork.getUnitRepeat().equals(EnumUnitRepeat.WEEK.getValue())) {
					
				} else if (newWork.getUnitRepeat().equals(EnumUnitRepeat.MONTH.getValue())) {
					
				} else {
					
				}
				
		}
		workDAO.save(newWork);
		return new WorkDTO(newWork);
	}
}
