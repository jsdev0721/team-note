package com.groupware.note.welfaremall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/welfaremall")
public class WelfareMallController {
	private final WelfareMallService welfareMallService;
	private final FileService fileService;
	
	@GetMapping("/list")
	public String findAll(Model model , @RequestParam(value = "page" , defaultValue = "0") int page) {
		model.addAttribute("list", this.welfareMallService.findAll(page, 9));
		return "welfaremallList";
	}
	
	@PostMapping("/list")
	public String findByProductName(Model model , @RequestParam(value = "page" , defaultValue = "0")int page ,@RequestParam(value = "productName")String productName) {
		model.addAttribute("list", this.welfareMallService.findByProductName(productName, page, 9));
		
		return "welfaremallList";
	}
	@GetMapping("/create")
	public String create(WelfareMallForm welfareMallForm) {
		return "welfaremallCreate";
	}
	@PostMapping("/create")
	public String create(@Valid WelfareMallForm welfareMallForm , BindingResult bindingResult) {
		WelfareMall welfareMall = new WelfareMall();
		welfareMall.setProductName(welfareMallForm.getProductName());
		welfareMall.setDescription(welfareMallForm.getDesciption());
		welfareMall.setPrice(welfareMallForm.getPrice());
		if(welfareMallForm.getFileList()!=null&&!welfareMallForm.getFileList().isEmpty()) {
			List<Files> fileList = new ArrayList<>();		
			for(MultipartFile multipartFile : welfareMallForm.getFileList()) {
				Files file = new Files();
				file = this.fileService.uploadFile(multipartFile);
				fileList.add(file);
			}
			welfareMall.setPhotos(fileList);
		}
		this.welfareMallService.save(welfareMall);
		return "redirect:/welfaremall/list";
	}
}
