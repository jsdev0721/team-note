package com.groupware.note.files;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class FileService {
	private final FileRepository fileRepository;
	
	//경로 고정설정
	@Value("${spring.servlet.multipart.location}")
	private String directory;
	//파일 확장자 return
	public String extendsFile(String originFileName) {
		int fileExtensionIndex = originFileName.lastIndexOf('.');
		return originFileName.substring(fileExtensionIndex + 1);
	}
	//jpg , jpeg , bmp , gif , png 타입일 경우 true return
	public boolean validFileExtension(String fileExtension) {
		String[] extension = {"jpg","jpeg","bmp","gif","png"};
		if(Arrays.stream(extension).anyMatch(value -> value.equals(fileExtension))) {
			return true;
		}
		return false;
	}
	//FilePath 설정 고정경로+저장이름+파일확장자 조합
	public String getFilePath(String originFileName , String storeFileName) {
		String fileType = extendsFile(originFileName);
		return directory+storeFileName+"."+fileType;
	}
	public String setFilePath(String originFileName , String storeFileName) {
		String fileType = extendsFile(originFileName);
		return directory+storeFileName+"."+fileType;
	}
	public String getPhotoPath(String originFileName , String storeFileName) {
		return directory+storeFileName;
	}

	//upload 메소드 -> List<Files> 으로 return
	public List<Files> uploadFile(List<MultipartFile> multipartFiles) {
		List<Files> files = new ArrayList<>();
		try {
			for(MultipartFile multipartFile : multipartFiles) {
				if(multipartFile.isEmpty()) {
					return null;
				}
				String originFileName = multipartFile.getOriginalFilename();
				String storeFileName = UUID.randomUUID().toString();
				while(!this.fileRepository.findByStoreFileName(storeFileName).isEmpty()) {
					storeFileName = UUID.randomUUID().toString();
				}
				String filePath = setFilePath(originFileName , storeFileName);
				Files _file = new Files();
				_file.setOriginFileName(originFileName);
				_file.setStoreFileName(storeFileName);
				multipartFile.transferTo(new File(filePath));
				files.add(this.fileRepository.save(_file));				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException("");
		}
		return files;
	}
	
	public Files uploadPhoto(MultipartFile multipartFile) {
		Files photo = new Files();
		try {
			String originFileName = multipartFile.getOriginalFilename();
			String storeFileName = UUID.randomUUID().toString();
			while(!this.fileRepository.findByStoreFileName(storeFileName).isEmpty()) {
				storeFileName = UUID.randomUUID().toString();
			}
			String filePath = setFilePath(originFileName, storeFileName);
			multipartFile.transferTo(new File(filePath));
			Thumbnailator.createThumbnail(new File(filePath), 50, 50);
			photo.setOriginFileName(originFileName);
			photo.setStoreFileName(storeFileName);
			this.fileRepository.save(photo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException("");
		}
		return photo;
	}
	
	//download 메소드 -> ResponseEntity 으로 return
	public ResponseEntity<Resource> downloadFile(Files file) {
		String filePath = getFilePath(file.getOriginFileName() , file.getStoreFileName());
		try {
			String decodePath = URLDecoder.decode(filePath, "UTF-8");
			Path fileLocation = Paths.get(decodePath).toAbsolutePath().normalize();
			Resource resource = new UrlResource(fileLocation.toUri());
			if(!resource.exists()) {
				throw new RuntimeException("FileNotFound"+filePath);
			}
			String fileName=URLEncoder.encode(file.getOriginFileName(), "UTF-8").replace("+", "%20");			
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + fileName + "\";")
					.body(resource);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	//사진 보이게 하는거
	public ResponseEntity<Resource> photoView(Files file) throws MalformedURLException{
		String photoPath = getPhotoPath(file.getOriginFileName() , file.getStoreFileName());
		UrlResource resource = new UrlResource("file:" + photoPath);
		return ResponseEntity
				.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);			
	}
	
	public Files findByFiles(Integer id) {
		Optional<Files> _file = this.fileRepository.findById(id);
		if(_file.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _file.get();
	}
	
}
