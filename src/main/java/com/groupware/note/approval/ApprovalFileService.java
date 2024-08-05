package com.groupware.note.approval;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
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

@Service
@RequiredArgsConstructor
public class ApprovalFileService {
	private final ApprovalFileRepository approvalFileRepository;
	
	@Value("${spring.servlet.multipart.location}")
	private String directory;
	
	public String getFilePath(ApprovalFile approvalFile) {
		int fileExtensionIndex = approvalFile.getOriginFileName().lastIndexOf('.');
		String fileType = approvalFile.getOriginFileName().substring(fileExtensionIndex + 1);
		return directory+"/"+approvalFile.getStoreFileName()+fileType;
	}
	public String setFilePath(String originFileName , String storeFileName) {
		int fileExtensionIndex = originFileName.lastIndexOf('.');
		String fileType = originFileName.substring(fileExtensionIndex + 1);
		return directory+"/"+storeFileName+fileType;
	}

	public void uploadFile(List<MultipartFile> multipartFiles , Approval Approval) {
		if(multipartFiles.isEmpty()) {
			return;
		}
		try {
			for(MultipartFile file : multipartFiles) {
				String originFileName = file.getOriginalFilename();
				String storeFileName = UUID.randomUUID().toString();
				String filePath = setFilePath(originFileName , storeFileName);
				ApprovalFile approvalFile = new ApprovalFile();
				approvalFile.setOriginFileName(originFileName);
				approvalFile.setStoreFileName(storeFileName);
				approvalFile.setApproval(Approval);
				file.transferTo(new File(filePath));
				this.approvalFileRepository.save(approvalFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ResponseEntity<Resource> downloadFile(ApprovalFile approvalFile) {
		String filePath = getFilePath(approvalFile);
		try {
			String decodePath = URLDecoder.decode(filePath , "UTF-8");
			Path fileLocation = Paths.get(decodePath).toAbsolutePath().normalize();
			Resource resource = new UrlResource(fileLocation.toUri());
			if(!resource.exists()) {
				throw new RuntimeException("FileNotFound"+filePath);
			}
			String fileName = URLEncoder.encode(approvalFile.getOriginFileName(), "UTF-8").replace("+", "%20");
			return ResponseEntity
					.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + fileName + "\";")
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public List<ApprovalFile> findByApproval(Approval approval) {
		return this.approvalFileRepository.findByApproval(approval);
	}
	
	public ApprovalFile findById(Integer id) {
		Optional<ApprovalFile> _approvalFile = this.approvalFileRepository.findById(id);
		if(_approvalFile.isEmpty()) {
			throw new DataNotFoundException("데이터를 찾을 수 없습니다.");
		}
		return _approvalFile.get();
	}
	
}
