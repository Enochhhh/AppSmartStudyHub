package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.focusedapp.smartstudyhub.dao.FilesDAO;
import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class CloudinaryService {

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Autowired
	private FilesDAO filesDAO;
	
	@Autowired
	UserService userService;

	/**
	 * Upload avatar User
	 * 
	 * @param file
	 * @param type
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public String uploadFile(MultipartFile file, String type, User user) throws IOException {

		Cloudinary cloudinary = new Cloudinary(
				ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret));

		String folder = ConstantUrl.URL_FOLDER + "/" + type + "/";


		// Set the transformation if needed (e.g., resizing, cropping, etc.)
		// Transformation transformation = new
		// Transformation().width(300).height(300).crop("fill");

		// Get Extension of file
		String extension = file.getOriginalFilename().split("\\.")[1];

		// Find top file to get Id
		Optional<Files> fileOptionalTopInDb = filesDAO.findTopByOrderByIdDesc();
		Files fileInDb = null;
		if (!fileOptionalTopInDb.isEmpty()) {
			fileInDb = fileOptionalTopInDb.get();
		}

		// Set the new file name in Cloudinary
		String newFileName;
		if (fileInDb != null) {
			newFileName = "File_" + user.getId() + "_" + (fileInDb.getId() + 1) + "_" + extension;
		} else {
			newFileName = "File_" + user.getId() + "_1_" + extension;
		}
		
		String publicId = newFileName;

		// Upload the image to Cloudinary
		Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
				ObjectUtils.asMap("folder", folder, "public_id", publicId));

		Files fileSave = Files.builder().folder(folder)
				.type(type)
				.fileName(newFileName).format((String) uploadResult.get("format"))
				.user(user)
				.resourceType((String) uploadResult.get("resource_type"))
				.secureUrl((String) uploadResult.get("secure_url"))
				.createdAt(new Date())
				.publicId((String) uploadResult.get("public_id"))
				.build();
		fileSave = filesDAO.save(fileSave);
		
		return fileSave.getSecureUrl();
	}
	
	/**
	 * Delete File in cloudinary
	 * 
	 * @param publicId
	 * @return
	 * @throws IOException
	 */
	public FilesDTO deleteFileInCloudinary(String publicId) throws IOException {
	
		Cloudinary cloudinary = new Cloudinary(
				ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret));

		Files file = filesDAO.findByPublicId(publicId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the file to delete!", "CloudinaryService -> deleteFileInCloudinary"));

		// Upload the image to Cloudinary
		cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

		filesDAO.delete(file);
		
		return new FilesDTO(file);
	}
	
}
