package com.groupware.note.department;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupware.note.position.PositionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DepartmentController {
	private final DepartmentService dService;
	private final PositionService pService;
	
	@GetMapping("/department/list")
	public String departmentList(Model model, @RequestParam(value = "deleteOn", defaultValue = "false" ) String deleteOn, @RequestParam(value = "deleteId", defaultValue = "0") Integer departmentId) {
		if(deleteOn.equals("true") && departmentId!=0) {
			this.dService.deleteDepartment(departmentId);
		}
		model.addAttribute("deleteOn", deleteOn);
		String OX = "notYet";
		model.addAttribute("OX", OX);
		List<Departments> depList = this.dService.findAll();
		model.addAttribute("depList", depList);
		return "admin/departmentList";
	}
	
	@PostMapping("/department/create")
	public String departmentCreate(Model model, @RequestParam(value = "createDep", defaultValue = "noString") String departmentName) {
		System.out.println(departmentName);
		String OX = this.dService.createNewDep(departmentName);
		if(OX.equals("fail")) {
			model.addAttribute("OX", OX);
			List<Departments> depList = this.dService.findAll();
			model.addAttribute("depList", depList);
			return "admin/departmentList";
		}
		return "redirect:/department/list";
	}
	
}
