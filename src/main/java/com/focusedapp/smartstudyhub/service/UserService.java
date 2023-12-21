package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtService;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtUser;
import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.exception.OTPCodeInvalidException;
import com.focusedapp.smartstudyhub.model.OtpCode;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.OAuth2UserInfo;
import com.focusedapp.smartstudyhub.model.custom.RankUserFocusDTO;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
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

		Optional<User> userTop = userDAO.findTopByOrderByIdDesc();
		User user = null;
		if (userTop.isEmpty()) {
			user = User.builder().firstName("#GUEST ").lastName(Integer.valueOf(1).toString())
					.createdAt(new Date()).role(EnumRole.GUEST.getValue()).status(EnumStatus.ACTIVE.getValue())
					.provider(Provider.LOCAL.getValue())
					.imageUrl(ConstantUrl.DEFAULT_IMAGE).build();
		} else {
			user = User.builder().firstName(Integer.valueOf(userTop.get().getId() + 1).toString()).lastName("#GUEST ")
					.createdAt(new Date()).role(EnumRole.GUEST.getValue()).status(EnumStatus.ACTIVE.getValue())
					.provider(Provider.LOCAL.getValue())
					.imageUrl(ConstantUrl.DEFAULT_IMAGE).build();
		}
		
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
	public RankUserFocusDTO rankByTimeFocusAllTime(Integer userId) {
		
		User user = findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		
		Comparator<User> comparator = Comparator.comparing(User::getTotalTimeFocus,
                Comparator.nullsFirst(Comparator.naturalOrder()));
		
		List<User> users = userDAO.findByStatus(EnumStatus.ACTIVE.getValue());
		users = users.stream()
				.sorted(comparator.reversed())
				.collect(Collectors.toList());
		
		Integer rank = 0;
		List<UserDTO> usersResponse = new ArrayList<>();
		UserDTO userCurrent = null;
		for (User userItem : users) {			
			UserDTO temp = new UserDTO(++rank, userItem);
			usersResponse.add(temp);	
			if (userItem.getId().equals(user.getId())) {
				userCurrent = temp;
			} 
		}
		
		return new RankUserFocusDTO(userCurrent, usersResponse);
	}
	
	/**
	 * Rank By Focus Previous Month
	 * 
	 * @param userId
	 * @return
	 */
	public RankUserFocusDTO rankByTimeFocusPreviousMonth(Integer userId) {
		
		User user = findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		
		Comparator<User> comparator = Comparator.comparing(User::getTotalTimeFocus,
                Comparator.nullsFirst(Comparator.naturalOrder()));
		
		List<User> users = userDAO.findByStatus(EnumStatus.ACTIVE.getValue());
		
		// Calculate Total Time Focus Previous Month
		users.stream().forEach(u -> {
			Long datemi = new Date().getTime();
			Long miliAbstract = 30L * 24L * 3600L * 1000L;
			Long dateMili = datemi - miliAbstract;
			Integer totalTimeFocusPreMonth = pomodoroService.calculateTotalTimeFocusPreviousMonth(u, new Date(dateMili));
			u.setTotalTimeFocus(totalTimeFocusPreMonth);
		});
		
		users = users.stream()
				.sorted(comparator.reversed())
				.collect(Collectors.toList());
		
		Integer rank = 0;
		List<UserDTO> usersResponse = new ArrayList<>();
		UserDTO userCurrent = null;
		for (User userItem : users) {			
			UserDTO temp = new UserDTO(++rank, userItem);
			usersResponse.add(temp);	
			if (userItem.getId().equals(user.getId())) {
				userCurrent = temp;
			} 
		}
		
		return new RankUserFocusDTO(userCurrent, usersResponse);
	}
	
}
