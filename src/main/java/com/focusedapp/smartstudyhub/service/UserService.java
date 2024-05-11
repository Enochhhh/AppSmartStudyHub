package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.focusedapp.smartstudyhub.config.jwtconfig.JwtService;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtUser;
import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.exception.OTPCodeInvalidException;
import com.focusedapp.smartstudyhub.exception.ValueExistedException;
import com.focusedapp.smartstudyhub.model.OtpCode;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.OAuth2UserInfo;
import com.focusedapp.smartstudyhub.model.custom.RankUserFocusDTO;
import com.focusedapp.smartstudyhub.model.custom.StatisticUsers;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.model.projectioninterface.RankUsersProjectionInterface;
import com.focusedapp.smartstudyhub.util.DateUtils;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.Provider;

@Service
public class UserService {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	MailSenderService mailSenderService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	OtpCodeService otpCodeService;
	
	@Autowired
	FilesService filesService;
	
	@Autowired
	PomodoroService pomodoroService;
	
	@Autowired
	FolderService folderService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	WorkService workService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	ThemeService themeService;
	
	@Autowired
	SoundConcentrationService soundConcentrationService;
	
	@Autowired
	SoundDoneService soundDoneService;
	
	@Autowired
	ThreadService threadService;

	/**
	 * 
	 * Check user existed by email
	 * 
	 * @param email
	 * @return
	 */
	public Boolean existsByEmailAndProviderLocal(String email) {
		return userDAO.existsByEmailAndProvider(email, Provider.LOCAL.getValue());
	}

	/**
	 * 
	 * Save user to database
	 * 
	 * @param user
	 * @return
	 */
	public User persistent(User user) {
		return userDAO.save(user);
	}

	/**
	 * Find User By Id And Status
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public User findByIdAndStatus(Integer id, String status) {
		return userDAO.findByIdAndStatus(id, status)
				.orElseThrow(() -> new NotFoundValueException("Not Found User By id: " + id.toString(),
						"UserService->findByIdAndStatus"));
	}
	
	/**
	 * Find User By Id
	 * 
	 * @param id
	 * @return
	 */
	public User findById(Integer userId) {
		return userDAO.findById(userId)
				.orElseThrow(() -> new NotFoundValueException("Not Found User By id: " + userId.toString(),
						"UserService->findById"));
	}

	/**
	 * 
	 * Find User by email and status ACTIVE and Provider Local
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmailAndProviderLocalAndStatus(String email, String status) {
		Optional<User> user = userDAO.findByEmailAndProviderAndStatus(email, Provider.LOCAL.getValue(), status);
		if (user.isEmpty()) {
			return null;
		}
		return user.get();
	}

	/**
	 * 
	 * Find User by email and Provider Local
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmailAndProviderAndStatusNotActiveE(String email, String provider) {
		Optional<User> user = userDAO.findByEmailAndProviderAndStatusNotActive(email, provider);
		if (user.isEmpty()) {
			return null;
		}
		return user.get();
	}

	/**
	 * 
	 * Find User by email
	 * 
	 * @param email
	 * @param statusFirst
	 * @param statusSecond
	 * @return
	 */
	public User findByEmail(String email) {
		User user = userDAO.findByEmail(email)
				.orElseThrow(() -> new NotFoundValueException("Not Found User By Email: " + email,
						"UserService->findByEmail"));
		return user;
	}

	/**
	 * 
	 * Create Guest User
	 * 
	 * @return
	 */
	public UserDTO createGuestUser() {

		User user = User.builder().lastName("#GUEST ")
					.createdAt(new Date()).role(EnumRole.GUEST.getValue()).status(EnumStatus.ACTIVE.getValue())
					.provider(Provider.LOCAL.getValue())
					.imageUrl(ConstantUrl.DEFAULT_IMAGE).build();	
		user = persistent(user);
		user.setFirstName(user.getId().toString());
		user.setTimeLastUse(new Date());
		user = persistent(user);
		return UserDTO.builder().firstName(user.getFirstName()).lastName(user.getLastName()).id(user.getId())
				.role(user.getRole()).createdAt(user.getCreatedAt().getTime())
				.imageUrl(user.getImageUrl()).build();
	}

	/**
	 * Send OTP Code to Email
	 * 
	 * @param request
	 */
	public void sendOtpEmailToUserLocal(String email) {

		OtpCode otpCode = otpCodeService.findByEmail(email);

		String subject = "Smart Study Hub send OTP Code for account " + email +":";
		String body = "<b>OTP Code is expired after 3 minutes</b> <br>" + "<p> Here is the OTP Code: "
				+ otpCode.getOtpCode() + "</p><br>";
		mailSenderService.sendEmail(email, subject, body);
	}

	/**
	 * Resend OTP Code
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public AuthenticationDTO resendOtpCodeToUserLocal(String email) {
		
		OtpCode otpCode = otpCodeService.findByEmail(email);
		if (otpCode == null) {
			otpCode = new OtpCode();
			otpCode.setEmail(email);
		}
		
		String otpCodeStr = otpCodeService.generateOtpCode();
		otpCode.setOtpCode(otpCodeStr);
		otpCode.setOtpTimeExpiration(new Date(new Date().getTime() + 180 * 1000));
		otpCodeService.persistent(otpCode);
		sendOtpEmailToUserLocal(email);

		return AuthenticationDTO.builder().otpCode(otpCodeStr).otpTimeExpiration(otpCode.getOtpTimeExpiration().getTime())
				.build();
	}

	/**
	 * Delete user by id
	 * 
	 * @param id
	 * @return
	 */
	public User deleteById(Integer id) {

		User user = userDAO.findById(id).orElseThrow(
				() -> new NotFoundValueException("Not Found the user to delete", "UserService->deleteById"));
		if (!user.getRole().equals(EnumRole.GUEST.getValue())) {
			return null;
		}
		userDAO.delete(user);

		return user;
	}

	/**
	 * Change Password when User forgot password
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public void changePassword(AuthenticationDTO authenticationDTO) {
		User user = findByEmailAndProviderLocalAndStatus(authenticationDTO.getEmail(), EnumStatus.ACTIVE.getValue());
		
		OtpCode otpCode = otpCodeService.findByEmail(authenticationDTO.getEmail());
		if (!otpCode.getOtpCode().equals(authenticationDTO.getOtpCode()) 
				|| otpCode.getOtpTimeExpiration().before(new Date())) {
			throw new OTPCodeInvalidException("OTP Code Invalid or Expired", "AuthenticationService -> register");
		}
		
		user.setPassword(passwordEncoder.encode(authenticationDTO.getPassword()));
		persistent(user);
	}

	/**
	 * Get User by Username and provider and status
	 * 
	 * @param userName
	 * @param status
	 * @return
	 */
	public User getUserByUsernameAndProvider(String userName, String provider) {
		User user = userDAO.findByUserNameAndProvider(userName, provider);
		return user;
	}

	/**
	 * Save data when login google in database
	 * 
	 * @param username
	 */
	public User processOAuthPostLogin(OAuth2UserInfo customOAuth2User) {
		String provider = customOAuth2User.getProvider();
		User newUser = null;

		String username = customOAuth2User.getId();
		String email = customOAuth2User.getEmail();
		if (email == null) { email = "";};
		
		newUser = getUserByUsernameAndProvider(username, provider);

		if (newUser == null) {
			newUser = new User();
			newUser.setUserName(customOAuth2User.getId());
			newUser.setProvider(provider);
			newUser.setStatus(EnumStatus.ACTIVE.getValue());
			newUser.setRole(EnumRole.CUSTOMER.getValue());
			newUser.setEmail(email);
			newUser.setImageUrl(customOAuth2User.getImageUrl());
			newUser.setCreatedAt(new Date());
			newUser.setFirstName(customOAuth2User.getName());
			newUser.setLastName("");

			newUser = persistent(newUser);
		} else {
			newUser.setImageUrl(customOAuth2User.getImageUrl());
			newUser.setFirstName(customOAuth2User.getName());
			newUser.setLastName("");
		}

		return newUser;
	}

	/**
	 * Generate token
	 * 
	 * @param user
	 * @return
	 */
	public String generateToken(User user) {
		return jwtService.generateToken(new JwtUser(user));
	}
	
	/**
	 * Find User by UserName and status
	 * 
	 * @param userName
	 * @param status
	 * @return
	 */
	public User findByUserNameAndStatus(String userName, String status) {
		User user = userDAO.findByUserNameAndStatus(userName, status)
				.orElseThrow(() -> new NotFoundValueException("Not Found the user by Username and status", "UserService->findByUserNameAndStatus"));
		
		return user;
	}
	
	/**
	 * 
	 * Check user existed by UserName
	 * 
	 * @param userName
	 * @return
	 */
	public Boolean existsByUserName(String userName) {
		return userDAO.existsByUserName(userName);
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public User findByUserName(String userName) {
		User user = userDAO.findByUserName(userName)
				.orElseThrow(() -> new NotFoundValueException("Not Found the user by Username", "UserService->findByUserName"));
		
		return user;
	}
	
	/**
	 * Change status user's account
	 * 
	 * @param user
	 * @param status
	 * @return
	 */
	public AuthenticationDTO changeStatus(Integer id, String status) {
		User user = userDAO.findById(id)
				.orElseThrow(() -> new NotFoundValueException("Not found the user by id", "UserService -> changeStatus"));
		
		user.setStatus(status);
		persistent(user);
		
		return AuthenticationDTO.builder()
				.email(user.getEmail())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.role(user.getRole())
				.createdAt(user.getCreatedAt().getTime())
				.build();
		
	}
	
	/**
	 * Mark Delete By UserId
	 * 
	 * @param userId
	 * @return
	 */
	public User markDeletedByUserId(Integer userId) {
		User user = findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		user.setStatus(EnumStatus.DELETED.getValue());		
		user = persistent(user);
		
		return user;
	}
	
	/**
	 * Check Password Correct
	 * 
	 * @param authenReq
	 * @param user
	 * @return
	 */
	public Boolean checkPasswordCorrect(AuthenticationDTO authenReq, User user) {
		
		if (passwordEncoder.matches(authenReq.getPassword(), user.getPassword())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Change Email User
	 * 
	 * @param authReq
	 * @param user
	 * @return
	 */
	public UserDTO changeEmailUser(AuthenticationDTO authReq, User user) {
		
		OtpCode otpCode = otpCodeService.findByEmail(authReq.getEmail());
		if (!otpCode.getOtpCode().equals(authReq.getOtpCode()) 
				|| otpCode.getOtpTimeExpiration().before(new Date())) {
			throw new OTPCodeInvalidException("OTP Code Invalid or Expired", "UserService -> changeEmailUser");
		}
		if (!user.getProvider().equals(Provider.LOCAL.getValue())) {
			return null;
		}
		user.setEmail(authReq.getEmail());
		user.setUserName(authReq.getEmail());
		user = userDAO.save(user);
		
		return new UserDTO(user);
	}
	
	/**
	 * Update Information User Customer
	 * 
	 * @param userInfo
	 * @param user
	 * @return
	 */
	public UserDTO updateInformationUserCustomer(UserDTO userInfo, User user) {
		
		if (user == null) {
			return null;
		}
		
		user.setPhoneNumber(userInfo.getPhoneNumber());
		user.setFirstName(userInfo.getFirstName());
		user.setLastName(userInfo.getLastName());
		user.setAddress(userInfo.getAddress());
		if (userInfo.getDateOfBirth() != null) {
			user.setDateOfBirth(new Date(userInfo.getDateOfBirth()));
		}
		user.setCountry(userInfo.getCountry());
		user.setImageUrl(userInfo.getImageUrl());
		user.setIsTwoFactor(userInfo.getIsTwoFactor());
		user.setCoverImage(userInfo.getCoverImage());
		
		user = userDAO.save(user);
		
		return new UserDTO(user);
	}
	
	/**
	 * Delete Current Avatar of User
	 * 
	 * @param user
	 * @return
	 */
	public UserDTO deleteCurrentAvatarUserCustomer(User user) {
		
		user.setImageUrl(ConstantUrl.DEFAULT_IMAGE);
		userDAO.save(user);
		return new UserDTO(user);
	}
	
	/**
	 * Rank By Focus All Time
	 * 
	 * @param userId
	 * @return
	 */
	public RankUserFocusDTO rankByTimeFocusAllTime(Integer userId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("totalTimeFocus").descending());
	
		UserDTO currentUserDto = null;
		Integer totalUsers = 0;
		if (page == 0) {
			User user = findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
			List<User> allUsers = userDAO.findByStatusAndRoleNot(EnumStatus.ACTIVE.getValue(), EnumRole.ADMIN.getValue(), 
					Sort.by("totalTimeFocus").descending());
			currentUserDto = new UserDTO(allUsers.indexOf(user) + 1, user);
			totalUsers = allUsers.size();
		}
		
		List<User> users = userDAO.findByStatusAndRoleNot(EnumStatus.ACTIVE.getValue(), EnumRole.ADMIN.getValue(), pageable);	
		List<UserDTO> usersResponse = new ArrayList<>();
		Integer rank = page * size;
		for (User userItem : users) {			
			UserDTO temp = new UserDTO(++rank, userItem);
			usersResponse.add(temp);	
		}
		
		return new RankUserFocusDTO(currentUserDto, usersResponse, totalUsers);
	}
	
	/**
	 * Rank By Focus Previous Month
	 * 
	 * @param userId
	 * @return
	 */
	public RankUserFocusDTO rankByTimeFocusPreviousMonth(Integer userId, Pageable pageable) {
		
		Integer totalUsers = userDAO.countByStatusAndRoleNot(EnumStatus.ACTIVE.getValue(), EnumRole.ADMIN.getValue());
		Long datemi = new Date().getTime();
		Long miliAbstract = 30L * 24L * 3600L * 1000L;
		Long dateMili = datemi - miliAbstract;
		List<RankUsersProjectionInterface> listUserRanked = userDAO.rankByTimeFocusPreviousMonth(new Date(dateMili), pageable);
		List<UserDTO> usersResponse = new ArrayList<>();
		UserDTO userCurrent = null;
		for (RankUsersProjectionInterface e : listUserRanked) {
			if (e.getId().equals(userId)) {
				userCurrent = new UserDTO();
				userCurrent.setId(e.getId());
				userCurrent.setRank(e.getRanks());
				userCurrent.setFirstName(e.getFirstName());
				userCurrent.setLastName(e.getLastName());
				userCurrent.setTotalTimeFocus(e.getTotalTimeFocus() == null ? 0 : e.getTotalTimeFocus());
				userCurrent.setImageUrl(e.getImageUrl());
				usersResponse.add(userCurrent);
			} else {
				UserDTO tempUser = new UserDTO();
				tempUser.setId(e.getId());
				tempUser.setRank(e.getRanks());
				tempUser.setFirstName(e.getFirstName());
				tempUser.setLastName(e.getLastName());
				tempUser.setTotalTimeFocus(e.getTotalTimeFocus() == null ? 0 : e.getTotalTimeFocus());
				tempUser.setImageUrl(e.getImageUrl());
				usersResponse.add(tempUser);
			}
		}
		
		return new RankUserFocusDTO(userCurrent, usersResponse, totalUsers);
	}
	
	/**
	 * Admin: Create User
	 * 
	 * @param request
	 * @return
	 */
	public UserDTO createUser(UserDTO request) {

		if (existsByUserName(request.getEmail()) 
				|| existsByEmailAndProviderLocal(request.getEmail())) {
			throw new ValueExistedException("Email Invalid or Existed", "UserService -> createUser");
		}
		String passwordGenerated = RandomStringUtils.randomAlphabetic(10);
		request.setPassword(passwordGenerated);
		User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.userName(request.getEmail())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).createdAt(new Date())
				.role(request.getRole())
				.phoneNumber(request.getPhoneNumber())
				.address(request.getAddress())
				.dateOfBirth(request.getDateOfBirth() != null ? new Date() : new Date(request.getDateOfBirth()))
				.country(request.getCountry())
				.provider(Provider.LOCAL.getValue())
				.imageUrl(StringUtils.isBlank(request.getImageUrl()) ? ConstantUrl.DEFAULT_IMAGE : request.getImageUrl())
				.status(EnumStatus.ACTIVE.getValue())
				.build();
		persistent(user);
		threadService.sendNewAccountToEmailUser(request);
		
		return new UserDTO(user);

	}
	
	/**
	 * Send New Account to Email
	 * 
	 * @param request
	 */
	public void sendNewAccountToEmailUser(UserDTO request) {

		String subject = "Smart Study Hub send to you the new account information";
		StringBuilder body = new StringBuilder();
		body.append("<b>Full Name: </b> <i>");
		body.append(request.getLastName());
		body.append(" "); body.append(request.getFirstName());
		body.append("</i> <br> <b>Email Login: </b> <i>"); 
		body.append(request.getEmail());
		body.append("</i> <br> <b>Password: </b> <i>");
		body.append(request.getPassword());
		body.append("</i> <br> <br> Please change your account password immediately to ensure the security of your account. <br>"); 
		body.append("Thank you.");
		
		mailSenderService.sendEmail(request.getEmail(), subject, body.toString());
	}
	
	/**
	 * Update User
	 * 
	 * @param request
	 * @return
	 */
	public UserDTO updateUser(UserDTO request) {

		User user = findById(request.getId());

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUserName(request.getEmail());
		user.setEmail(request.getEmail());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setAddress(request.getAddress());
		user.setDateOfBirth(request.getDateOfBirth() != null ? new Date() : new Date(request.getDateOfBirth()));
		user.setCountry(request.getCountry());
		user.setImageUrl(request.getImageUrl());
		user.setRole(request.getRole());
			
		persistent(user);	
		return new UserDTO(user);
	}
	
	/**
	 * Mark Status User Admin
	 * 
	 * @param request
	 * @return
	 */
	public UserDTO markStatus(UserDTO request) {

		User user = findById(request.getId());
		user.setStatus(request.getStatus());
		if (user.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
			user.setTimeAdminModified(null);
		} else if (user.getStatus().equals(EnumStatus.DELETED.getValue())
				|| user.getStatus().equals(EnumStatus.BANNED.getValue())) {
			user.setTimeAdminModified(new Date());
		}		
		persistent(user);	
		return new UserDTO(user);
	}
	
	/**
	 * Mark Status User Admin
	 * 
	 * @param request
	 * @return
	 */
	public List<UserDTO> getAll(Pageable pageable) {

		List<User> users = userDAO.findByRoleNot(EnumRole.ADMIN.getValue(), pageable);	
		return users.stream()
				.map(u -> new UserDTO(u))
				.collect(Collectors.toList());
	}
	
	/**
	 * Delete User by Admin
	 * 
	 * @param id
	 * @return
	 */
	public UserDTO adminDeleteById(Integer id) {
		
		User user = userDAO.findById(id).orElseThrow(
				() -> new NotFoundValueException("Not Found the user to delete", "UserService->adminDeleteById"));
		// Delete All Files of User
		userDAO.delete(user);

		return new UserDTO(user);
	}
	
	/**
	 * 
	 * Clean Data User
	 * 
	 * @param userId
	 * @return
	 */
	public void cleanData(User user) throws IOException {
		
		folderService.deleteAllFolderOfUser(user);
		projectService.deleteAllProjectOfUser(user);
		workService.deleteAllWorksOfUser(user);
		pomodoroService.deleteAllPomodorosOfUser(user);
		tagService.deleteAllTagsOfUser(user);
		
		threadService.deleteAllThemesOfUser(user);
		threadService.deleteAllSoundsConcentrationOfUser(user);
		threadService.deleteAllSoundsDoneOfUser(user);;
		threadService.deleteAllFilesUser(user);
		threadService.clearInfo(user);
	}
	
	public void clearInfo(User user) {
		user.setTotalWorks(null);
		user.setIsTwoFactor(false);
		user.setTotalPomodoros(null);
		user.setTotalTimeFocus(null);
		persistent(user);
	}
	
	/**
	 * Count Users by role not
	 * 
	 * @param role
	 * @return
	 */
	public Integer countByRoleNot(String role) {
		return userDAO.countByRoleNot(role);
	}
	
	/**
	 * Update time last use of Guest
	 * 
	 * @param guestId
	 * @return
	 */
	public Boolean updateTimeLastUseOfGuest(Integer guestId) {
		User user = findByIdAndStatus(guestId, EnumStatus.ACTIVE.getValue());
		if (!user.getRole().equals(EnumRole.GUEST.getValue())) {
			return false;
		}
		user.setTimeLastUse(new Date());
		persistent(user);
		return true;
	}
	
	public void persistentAll(List<User> users) {
		userDAO.saveAll(users);
	}
	
	/**
	 * Check and reset due date premium of users have registed
	 * 
	 */
	public void resetDueDatePremium() {
		List<User> users = userDAO.findByRole(EnumRole.PREMIUM.getValue());
		
		Date nowDate = new Date();
		if (!CollectionUtils.isEmpty(users)) {
			users.stream().forEach(user -> {
				if (user.getDueDatePremium().getTime() <= nowDate.getTime()) {
					sendNotificationDueDatePremiumToEmailUser(user);
					user.setRole(EnumRole.CUSTOMER.getValue());
					user.setDueDatePremium(null);
				}
			});
			persistentAll(users);
		}
	}
	/**
	 * Send notification due date premium to Email User
	 * 
	 * @param userId
	 * @param dateExpired
	 */
	public void sendNotificationDueDatePremiumToEmailUser(User user) {
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
		
		String subject = "Notice that the account has expired subscription to the Premium package";
		StringBuilder body = new StringBuilder();
		body.append("<i>Dear ");
		body.append(user.getLastName());
		body.append(" "); body.append(user.getFirstName());
		body.append(", </i><br>"); 
		body.append("<i>We regret to inform you that your Premium subscription expired on ");
		body.append(formatter.format(user.getDueDatePremium()));
		body.append(". Please renew at the app if you want to continue using the service.</i><br>");
		body.append("<i>Thank you and wish you good health.</i><br><br>");
		body.append("<i>From SmartStudyHub</i>");
		
		mailSenderService.sendEmail(user.getEmail(), subject, body.toString());
	}
	
	/**
	 * Statistic User By Date Range For Admin
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public List<StatisticUsers> statisticUserByDateRangeForAdmin(Long dateFrom, Long dateTo) {
		List<User> users = userDAO.statisticUserByDateRangeForAdmin(new Date(dateFrom), new Date(dateTo));
		Map<Long, Long> mapTotalUsers = new HashMap<>();
		if (!CollectionUtils.isEmpty(users)) {
			mapTotalUsers = users.stream()
					.collect(Collectors.groupingBy(u -> {
						return DateUtils.setTimeOfDateToMidnight(u.getCreatedAt().getTime()).getTime();
					}, Collectors.counting()));
		}
		List<StatisticUsers> statisticUsers = new ArrayList<>();
		for (Map.Entry<Long, Long> entry : mapTotalUsers.entrySet()) {
			statisticUsers.add(new StatisticUsers(entry.getKey(), entry.getValue()));
		}
		return statisticUsers;
	}
	
	/**
	 * Statistic User By Month Range For Admin
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public List<StatisticUsers> statisticUserByMonthRangeForAdmin(Long dateFromMili, Long dateToMili) {
		Date dateFrom = new Date(dateFromMili);
		Date dateTo = new Date(dateToMili);
		List<User> users = userDAO.statisticUserByDateRangeForAdmin(dateFrom, dateTo);
		
		List<String> keyGroup = new ArrayList<>();
		Date currentDate = dateFrom;
		do {
			String key = String.valueOf(currentDate.getTime()).concat(", ");
			LocalDateTime currentDateTimeZone = DateUtils.convertoToLocalDateTime(currentDate);
			int dayOfWeek = currentDateTimeZone.getDayOfWeek().getValue() + 1;
			int distanceWillAdd = 8 - dayOfWeek;
			Date newDate = DateUtils.addDaysForDate(currentDate, distanceWillAdd);
			if (newDate.getTime() <= dateTo.getTime()) {
				key = key.concat(String.valueOf(newDate.getTime()));
				currentDate = DateUtils.addDaysForDate(newDate, 1);
			} else {
				currentDate = dateTo;
				key = key.concat(String.valueOf(currentDate.getTime()));
			}			
			keyGroup.add(key);
		} while(currentDate.getTime() < dateTo.getTime());
		
		List<StatisticUsers> statisticUsers = new ArrayList<>();
		if (!CollectionUtils.isEmpty(users)) {
			for (String dateRange : keyGroup) {
				Long from = Long.valueOf(dateRange.split(", ")[0]);
				Long to = Long.valueOf(dateRange.split(", ")[1]);
				Long toValueForFilter = to + 24 * 60 * 60 * 1000;
				
				Long countingUser = users.stream()
						.filter(u -> u.getCreatedAt().getTime() >= from && u.getCreatedAt().getTime() < toValueForFilter)
						.collect(Collectors.counting());
				statisticUsers.add(new StatisticUsers(from, to, countingUser));
			}
		}
		return statisticUsers;
	}
	
	/**
	 * Search User
	 * 
	 * @param fullName
	 * @param email
	 * @param user
	 * @return
	 */
	public List<UserDTO> searchUserForAdmin(String key) {
		List<User> users = userDAO.findByRoleNot(EnumRole.ADMIN.getValue());
		if (!CollectionUtils.isEmpty(users)) {
			return users.stream()
					.filter(u -> u.getId().toString().contains(key) 
							|| (StringUtils.isNotBlank(u.getEmail()) && u.getEmail().contains(key))
							|| (u.getLastName() != null && u.getFirstName() != null 
							&& u.getLastName().concat(" ").concat(u.getFirstName()).strip().contains(key)))
					.map(u -> new UserDTO(u))
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	/**
	 * Get UserDTO by User Id
	 * 
	 * @param userId
	 * @return
	 */
	public UserDTO getById(Integer userId) {
		User user = findById(userId);
		return new UserDTO(user);
	}
	
	/**
	 * Get UserDTO by UserId and Status
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public UserDTO getByIdAndStatus(Integer userId, String status) {
		return new UserDTO(findByIdAndStatus(userId, status));
	}
	
	/**
	 * Reset data of User Daily
	 * 
	 */
	public void resetDataOfUserDaily() {
		List<User> users = userDAO.findUsersToResetDataDaily();
		if (!CollectionUtils.isEmpty(users)) {
			users.stream().forEach(u -> {
				u.setTotalWorksToday(null);
				u.setTotalPomodorosToday(null);
				u.setTotalTimeFocusToday(null);
			});
			userDAO.saveAll(users);
		}
	}
	
	/**
	 * Reset data of User Weekly
	 * 
	 */
	public void resetDataOfUserWeekly() {
		List<User> users = userDAO.findUsersToResetDataWeekly();
		if (!CollectionUtils.isEmpty(users)) {
			users.stream().forEach(u -> {
				u.setTotalWorksWeekly(null);
				u.setTotalPomodorosWeekly(null);
				u.setTotalTimeFocusWeekly(null);
			});
			userDAO.saveAll(users);
		}
	}
	
	public List<User> findUsersToResetDataDaily() {
		return userDAO.findUsersToResetDataDaily();
	}
}
