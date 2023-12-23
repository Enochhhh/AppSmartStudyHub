package com.focusedapp.smartstudyhub.service;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.focusedapp.smartstudyhub.model.custom.WorkResponseDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkScheduleDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkSortedResponseDTO;
import com.focusedapp.smartstudyhub.util.comparator.SortByPriorityComparator;
import com.focusedapp.smartstudyhub.util.comparator.SortByProjectNameComparator;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPriority;
import com.focusedapp.smartstudyhub.util.enumerate.EnumSortType;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusWork;
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
				.timeWillStart(dataCreate.getTimeWillStart() == null ? null : new Date(dataCreate.getTimeWillStart()))
				.priority(dataCreate.getPriority() == null ? EnumPriority.NONE.getValue() : dataCreate.getPriority())
				.numberOfPomodoros(dataCreate.getNumberOfPomodoros() == null ? 0 : dataCreate.getNumberOfPomodoros())
				.timeOfPomodoro(dataCreate.getTimeOfPomodoro() == null ? 25 : dataCreate.getTimeOfPomodoro())
				.isRemindered(Boolean.FALSE)
				.isRepeated(Boolean.FALSE)
				.numberOfPomodorosDone(dataCreate.getNumberOfPomodorosDone() == null ? 0 : dataCreate.getNumberOfPomodorosDone())
				.timePassed(dataCreate.getTimePassed() == null ? 0 : dataCreate.getTimePassed())
				.tags(tags)
				.createdDate(new Date())
				.status(EnumStatus.ACTIVE.getValue())
				.assignee(dataCreate.getAssigneeId() == null ? null : 
					userService.findByIdAndStatus(dataCreate.getAssigneeId(), EnumStatus.ACTIVE.getValue()))
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
		workDb.setTimeWillStart(dataUpdate.getTimeWillStart() == null ? null : new Date(dataUpdate.getTimeWillStart()));
		workDb.setTimeWillAnnounce(dataUpdate.getTimeWillAnnounce() == null ? null : new Date(dataUpdate.getTimeWillAnnounce()));
		workDb.setPriority(dataUpdate.getPriority());
		workDb.setNumberOfPomodoros(dataUpdate.getNumberOfPomodoros());
		workDb.setTimeOfPomodoro(dataUpdate.getTimeOfPomodoro());
		workDb.setIsRemindered(dataUpdate.getIsRemindered());
		workDb.setIsRepeated(dataUpdate.getIsRepeated());
		workDb.setNote(dataUpdate.getNote());	
		workDb.setAssignee(dataUpdate.getAssigneeId() == null ? null : 
					userService.findByIdAndStatus(dataUpdate.getAssigneeId(), EnumStatus.ACTIVE.getValue()));
		// workDb.setNumberOfPomodorosDone(dataUpdate.getNumberOfPomodorosDone());
		if (dataUpdate.getStartTime() != null) {
			workDb.setStartTime(new Date(dataUpdate.getStartTime()));
		}
		if (dataUpdate.getEndTime() != null) {
			workDb.setEndTime(new Date(dataUpdate.getEndTime()));
		}		
		workDb.setTags(tagService.findByIds(tagIds));
		workDb.setStatus(dataUpdate.getStatus() == null ? EnumStatus.ACTIVE.getValue() : dataUpdate.getStatus());
				
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
		workDb.setIsRepeated(false);
		workDb.setEndTime(new Date());
		if (workDb.getStartTime() == null) {
			workDb.setStartTime(new Date());
		}
		
		PomodoroDTO pomodoroRequest = PomodoroDTO.builder()
				.userId(workDb.getUser().getId())
				.workId(workDb.getId())
				.endTime(new Date().getTime())
				.isEndPomo(true)
				.build();
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
			
			List<ExtraWork> extraWorks = work.getExtraWorks() == null ? new ArrayList<>() : work.getExtraWorks();
			extraWorks.stream()
				.forEach(ew -> {
					if (ew.getStatus().equals(EnumStatus.COMPLETED.getValue())) {
						extraWorkService.recover(ew.getId());
					}
				});					
		} else if (work.getStatus().equals(EnumStatus.DELETED.getValue())) {
			work.setStatus(work.getOldStatus());
			work.setOldStatus(null);
			
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
			works = workDAO.findByWorkNameContainingAndUserIdAndStatus(keySearch, userId, EnumStatus.ACTIVE.getValue());
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
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.THISWEEK.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.NEXT7DAY.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.NEXT7DAY.getValue()))
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
	 * Get Works Schedule
	 * 
	 * @param date
	 * @param userId
	 * @return
	 */
	public WorkScheduleDTO getWorkSchedule(Long date, Integer userId) {
		
		List<Work> works = workDAO.findByUserIdAndOneOfTwoStatus(userId, EnumStatus.ACTIVE.getValue(), 
				EnumStatus.COMPLETED.getValue());
		List<WorkDTO> worksConvert = works.stream()
				.map(w -> new WorkDTO(w))
				.collect(Collectors.toList());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateReq = new Date(date);
		Boolean isToday = formatter.format(new Date()).equals(formatter.format(dateReq)) ? true : false;
		
		List<WorkDTO> listWorkActive = new ArrayList<>();
		List<WorkDTO> listWorkCompleted = new ArrayList<>();
		List<WorkDTO> listWorkDueDate = new ArrayList<>();
		List<WorkDTO> listWorkOutOfDate = new ArrayList<>();
		
		for (WorkDTO work : worksConvert) {
			
			if (isToday && work.getStatusWork().equals(EnumStatusWork.OUTOFDATE.getValue())) {
				listWorkOutOfDate.add(work);
			} else if (work.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
				if (formatter.format(new Date(work.getTimeWillStart())).equals(formatter.format(dateReq))) {
					listWorkActive.add(work);
				}
				if (formatter.format(new Date(work.getDueDate())).equals(formatter.format(dateReq))) {
					listWorkDueDate.add(work);
				}
				continue;
			} else if (work.getStatus().equals(EnumStatus.COMPLETED.getValue()) 
					&& formatter.format(new Date(work.getEndTime())).equals(formatter.format(dateReq))) {
				listWorkCompleted.add(work);
			} 
		}
		
		return new WorkScheduleDTO(date, userId, listWorkActive, listWorkCompleted, listWorkDueDate, listWorkOutOfDate);
				
	}
	
	/**
	 * Get Work Completed of User
	 * 
	 * @param userId
	 * @return
	 */
	public List<WorkDTO> getWorkCompletedOfUser(Integer userId) {
		
		List<Work> works = workDAO.findByUserIdAndStatus(userId, EnumStatus.COMPLETED.getValue());
		
		return works.stream()
				.map(w -> new WorkDTO(w))
				.collect(Collectors.toList());	
		
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
			Map<Long, List<WorkDTO>> mapWork = works.stream()	
					.collect(Collectors.groupingBy(w -> w.getDueDate(),Collectors.toList()));
			
			mapWork = mapWork.entrySet().stream()
					.sorted(Map.Entry.<Long, List<WorkDTO>>comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			
			for (Map.Entry<Long, List<WorkDTO>> entry : mapWork.entrySet()) {
				List<WorkDTO> worksRes = entry.getValue().stream()
						.sorted(comparator.reversed())
						.collect(Collectors.toList());
				workSortedList.add(new WorkSortedResponseDTO(entry.getKey(), worksRes));
			}
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
	 * Get Time and Number Work Active by type
	 * 
	 * @param type
	 * @return
	 */
	/*
	public WorkResponseDTO getTimeAndNumberWorkActiveByType(Integer userId) {
		
		List<Work> listWorkActive = workDAO.findByUserIdAndOneOfTwoStatus(userId, EnumStatus.ACTIVE.getValue(), EnumStatus.COMPLETED.getValue());
		if (CollectionUtils.isEmpty(listWorkActive)) {
			return new WorkResponseDTO(new ArrayList<>());
		}
		List<WorkDTO> worksConvert = listWorkActive.stream()
										.map(w -> new WorkDTO(w))
										.collect(Collectors.toList());
		
		Integer timeWorkToday = 0;
		Integer numberWorkActiveToday = 0;
		
		Integer timeWorkOutOfDate = 0;
		Integer numberWorkActiveOutOfDate = 0;
		
		Integer timeWorkTomorrow = 0;
		Integer numberWorkActiveTomorrow = 0;
		
		Integer timePassedThisWeek = 0;
		Integer numberWorkCompletedThisWeek = 0;
		
		Integer timePassedNext7Day = 0;
		Integer numberWorkCompletedNext7Day = 0;
		
		Integer timePassedSomeDay = 0;
		Integer numberWorkCompletedSomeDay= 0;
		
		Integer timePassedAll = 0;
		Integer numberWorkCompletedAll = 0;
		
		Integer timePassedTaskDefault= 0;
		Integer numberWorkCompletedTaskDefault = 0;
		
		Integer timePassedPlanned = 0;
		Integer numberWorkCompletedPlanned = 0;
		
		for (WorkDTO work : worksConvert) {
			if (work.getStatusWork().equals(EnumStatusWork.TODAY.getValue())
					|| work.getStatusWork().equals(EnumStatusWork.OUTOFDATE.getValue())) {
				timePassedToday += work.getTimePassed();
				if (work.getStatus().equals(numberWorkCompletedPlanned)))
			}
		}
		
	
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
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.THISWEEK.getValue()))
					.sorted(comparator.reversed())
					.collect(Collectors.toList());
		} else if (type.equals(EnumStatusWork.NEXT7DAY.getValue())) {
			worksConvert = worksConvert.stream()
					.filter(w -> w.getStatusWork().equals(EnumStatusWork.NEXT7DAY.getValue()))
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
	*/

}
