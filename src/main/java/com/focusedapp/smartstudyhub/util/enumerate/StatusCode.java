package com.focusedapp.smartstudyhub.util.enumerate;

public enum StatusCode {
	
	// System - featureCode: 0
	SUCCESS("0_1_s", "Success"),
	SYSTEM_FAILURE("0_2_f", "System Failure"),
	PARAMETER_INVALID("0_3_f", "Parameter Invalid"),
	DATA_EMPTY("0_4_f", "Data Empty"),
	DATA_NOT_FOUND("0_5_f", "Data Not Found"),
	FAIL("0_6_f", "Failed"),
	DATA_EXISTED("0_7_f", "Data Existed"),
	AUTHENTICATED_FAILURE("0_8_f", "Authenticated Failure"),
	PARSE_ERROR("0_9_f", "Parse Error"),
	UNAUTHORIZED("0_10_f", "Unauthorized"),
	TOKEN_INVALID("0_11_f", "Token Invalid"),
	
	// User - featureCode: 1
	/* Register */
	REGISTER_SUCCESS("1_1_s", "Register Successfully"),
	REGISTER_FAILURE("1_1_f", "Register Failure"),
	RESEND_OTP_FAILURE("1_2_f", "Resend OTP Code Failure"),
	RESEND_OTP_SUCCESS("1_2_s", "Resend OTP Code Success"),
	/* Login */
	LOGIN_SUCCESS("2_1_s", "Login Success"),
	LOGIN_FAILURE("2_1_f", "Login Failure"),
	CHANGE_PASSWORD_SUCCESS("2_2_s", "Change Password Success"),
	CHANGE_PASSWORD_FAILURE("2_2_f", "Change Password Failure"),
	ACCOUNT_BANNED("2_3_f", "Account was banned"),
	ACCOUNT_DELETED("2_4_f", "Account was deleted"),
	OTP_CODE_INVALID("2_5_f", "OTP Code Invalid"),
	/* GUEST User */
	AUTHENTICATION_REQUIRE("3_1_f", "Authentication is required!"),
	/* CUSTOMER User */
	CHANGE_EMAIL_NOT_LOCAL("4_1_f", "Change Email of User that is not LOCAL!"),
	UPDATE_INFO_FAILURE("4_2_f", "Upate User's Information Unsuccessfully!"),
	/* FOLDER */
	DELETE_FOLDER_COMPLETELY_FAILURE("5_1_f", "Delete Folder Completely Failure!"),
	MARK_COMPLETED_FOLDER_FAILURE("5_2_f", "Mark Completed Folder Failure!"),
	RECOVER_FOLDER_FAILURE("5_3_f", "Recover Folder Failure!"),
	DELETE_ALL_FOLDERS_COMPLETELY_FAILURE("5_4_f", "Delete All Folders Completely Failure!"),
	/* PROJECT */
	DELETE_PROJECT_COMPLETELY_FAILURE("6_1_f", "Delete Project Completely Failure!"),
	MARK_COMPLETED_PROJECT_FAILURE("6_2_f", "Mark Completed Project Failure!"),
	RECOVER_PROJECT_FAILURE("6_3_f", "Recover Folder Failure!"),
	MARK_DELETED_PROJECT_FAILURE("6_4_f", "Mark Deleted Project Failure!"),
	DELETE_ALL_PROJECTS_COMPLETELY_FAILURE("6_5_f", "Delete All Projects Completely Failure!"),
	/* WORK */
	MARK_DELETED_WORK_FAILURE("7_1_f", "Mark Deleted Work Failure!"),
	MARK_COMPLETED_WORK_FAILURE("7_2_f", "Mark Completed Work Failure!"),
	RECOVER_WORK_FAILURE("7_3_f", "Recover Work Failure!"),
	REPEAT_WORK_FAILURE("7_4_f", "Repeat Work Failure!"),
	DELETE_ALL_WORKS_COMPLETELY_FAILURE("7_5_f", "Delete All Works Completely Failure!"),
	/* EXTRA WORK */
	MARK_DELETED_EXTRAWORK_FAILURE("8_1_f", "Mark Deleted Extra Work Failure!"),
	MARK_COMPLETED_EXTRAWORK_FAILURE("8_2_f", "Mark Completed Extra Work Failure!"),
	RECOVER_EXTRAWORK_FAILURE("8_3_f", "Recover Extra Work Failure!"),
	/* TAG */
	DELETE_TAG_FAILURE("9_1_f", "Mark Deleted Tag Failure!"),
	/* UPLOAD FILE */
	MISSING_FILE("10_1_f", "Missing file when upload!"),
	
	// Admin - featureCode: 11
	CREATE_USER_FAILURE("11_1_f", "Create User Failure!"),
	SEARCH_USER_FAILURE("11_2_f", "Search User Failure!"),
	ADMIN_GET_REPORTS_FAILURE("11_3_f", "Get Reports Failure!"),
	ADMIN_DELETE_REPORT_FAILURE("11_4_f", "Delete Report Failure!"),
	
	/* REPORT */
	GET_REPORTS_FAILURE("12_1_f", "Get Reports Failure!"),
	GET_DETAIL_REPORT_FAILURE("12_2_f", "Get Detail Report Failure!"),
	
	/* EVENT */
	CREATE_EVENT_FAILURE("13_1_f", "Create Event Failure!"),
	GET_DETAIL_EVENT_FAILURE("13_2_f", "Get Detail Event Failure!"),
	UPDATE_EVENT_FAILURE("13_3_f", "Update Event Failure!"),
	DELETE_EVENT_FAILURE("13_4_f", "Delete Event Failure!"),
	GET_TIME_LINE_EVENT_FAILURE("13_5_f", "Get Time Line Event Failure!"),
	
	/* HISTORY DAILY */
	GET_HISTORY_DAILY_FAILURE("14_1_f", "Get History Daily Failure!"),
	DELETE_HISTORY_DAILY_FAILURE("14_2_f", "Delete History Daily Failure!"),
	DELETE_ALL_HISTORY_DAILIES_FAILURE("14_3_f", "Delete All History Dailies Failure!"),
	
	/* STATISTICAL */
	STATISTICAL_TIME_FOCUS_FAILURE("15_1_f", "Statistical Time Focus Failure!"),
	STATISTICAL_TIME_FOCUS_BY_WORK_FAILURE("15_2_f", "Statistical Time Focus By Work Failure!"),
	STATISTICAL_WORK_FAILURE("15_3_f", "Statistical Work Failure!");
	
	private final String code;
	private final String message;
	
	private StatusCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
