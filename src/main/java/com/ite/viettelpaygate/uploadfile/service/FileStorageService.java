package com.ite.viettelpaygate.uploadfile.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.ite.viettelpaygate.uploadfile.exception.FileStorageException;
import com.ite.viettelpaygate.uploadfile.exception.MyFileNotFoundException;
import com.ite.viettelpaygate.uploadfile.property.FileStorageProperties;

import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Không thể tạo thư mục để upload file.", ex);
		}
	}

	public String storeFile(MultipartFile file) {

		String fileExtentions = ".txt,.xls,.xlsx,.xlsm,.doc,.docx,.pdf,.jpg,.png";
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		int lastIndex = fileName.lastIndexOf('.');
		String substring = fileName.substring(lastIndex, fileName.length());
		if (!fileExtentions.contains(substring)) {
			throw new FileStorageException(
					"Upload file thất bại! file để upload phải có định dạng ( " + fileExtentions + " )");
		} else {
			try {
				// Check if the file's name contains invalid characters
				if (fileName.contains("..")) {
					throw new FileStorageException("Thất bại! Tên file chưa đường dẫn không hợp lệ " + fileName);
				}

				// Copy file to the target location (Replacing existing file with the same name)
				Path targetLocation = this.fileStorageLocation.resolve(fileName);
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

				return fileName;
			} catch (IOException ex) {
				throw new FileStorageException("Không thể lưu file " + fileName + ". Xin vui lòng thử lại!", ex);
			}
		}

	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("Không tìm thấy file " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("Không tìm thấy file " + fileName, ex);
		}
	}
}
