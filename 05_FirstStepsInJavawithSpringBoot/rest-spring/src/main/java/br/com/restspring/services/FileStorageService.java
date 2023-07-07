package br.com.restspring.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.restspring.config.FileStorageConfig;
import br.com.restspring.exceptions.FileStorageException;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath()
				.normalize();

		this.fileStorageLocation = path;

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not create the directory where the upload files will be storaged",
					e);
		}
	}

	public String storeFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (filename.contains("..")) {
				throw new FileStorageException(
						"Sorry! Filename contains invalid path sequence "
								+ file);
			}
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			
			Files.copy(file.getInputStream(), targetLocation,
					StandardCopyOption.REPLACE_EXISTING);
			
			return filename;
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not store file " + filename + ". Please try again!",
					e);
		}
	}

}
