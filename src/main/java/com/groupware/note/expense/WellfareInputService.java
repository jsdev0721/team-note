package com.groupware.note.expense;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groupware.note.department.DepartmentRepository;
import com.groupware.note.department.Departments;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsRepository;
import com.groupware.note.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WellfareInputService {
	private final UserRepository uRepo;
	private final UserDetailsRepository udRepo;
	private final DepartmentRepository dRepo;
	private final WellfarePointInputRepositiory wpiRepo;
	
	//매 날짜마다 부서당 인원수 체크해서 해당 숫자만큼 입력
	@Scheduled(cron = " * * 10 1W 3,9 * " )
	public void pointInputToDep() {
		for(Departments dep : this.dRepo.findAll()) {
			
			long pByDep = (this.uRepo.findByDep(dep.getDepartmentName()).size()
					* this.wpiRepo.findById(1).get().getDepPointPer()  )
					+  this.wpiRepo.findById(1).get().getDepPointPlus();
			dep.setPoints(pByDep);
			this.dRepo.save(dep);
		}
	}
	//매 날짜마다 각 사원에게 입력
	@Scheduled(cron = " * 10 9 1W * * " )
	public void pointInputToWorker() {
		for(UserDetails ud : this.udRepo.findAll()) {
			Long pByWor = this.wpiRepo.findById(1).get().getIndividualPoint();
			ud.setPoints(pByWor);
			this.udRepo.save(ud);
		}
	}
	
	public WellfarePointInput wellfarePointInput() {
		WellfarePointInput pointInput = this.wpiRepo.findById(1).get();
		return pointInput;
	}
	
	public void updatePoint(Long depPointPer, Long depPointPlus, Long individualPoint) {
		WellfarePointInput pointInput = this.wpiRepo.findById(1).get();
		pointInput.setDepPointPer(depPointPer);
		pointInput.setDepPointPlus(depPointPlus);
		pointInput.setIndividualPoint(individualPoint);
		this.wpiRepo.save(pointInput);
	}
	
	public List<String> calDepPoint() {
		List<String> list = new ArrayList<>();
		for(Departments dep : this.dRepo.findAll()) {
			Long pByDep = (this.uRepo.findByDep(dep.getDepartmentName()).size()
					* this.wpiRepo.findById(1).get().getDepPointPer()  )
					+  this.wpiRepo.findById(1).get().getDepPointPlus();
			String _deppoint =  dep.getDepartmentName() + " 에 입력될 포인트 : " + Long.toString(pByDep);
			list.add(_deppoint);
		}
		
		return list;
	}
	
	public Long calIndPoint() {
		Long pByWor = this.wpiRepo.findById(1).get().getIndividualPoint();
		return pByWor;
	}
	
}
