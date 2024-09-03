package com.groupware.note.expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	@Scheduled(cron = " 0 0 10 1W 3,9 * " )
	public void pointInputToDep() {
		for(Departments dep : this.dRepo.findAll()) {
			Optional<WellfarePointInput> pointInput = this.wpiRepo.findById(1);
			if(pointInput.isPresent()) {
				long pByDep = (this.uRepo.findByDep(dep.getDepartmentName()).size()
						* pointInput.get().getDepPointPer()  )
						+  pointInput.get().getDepPointPlus();
				dep.setPoints(pByDep);
				this.dRepo.save(dep);
			}
		}
	}

	//매 날짜마다 각 사원에게 입력
	@Scheduled(cron = " 0 10 9 1W * * " )
	public void pointInputToWorker() {
		for(UserDetails ud : this.udRepo.findAll()) {
			Optional<WellfarePointInput> pointInput = this.wpiRepo.findById(1);
				if(pointInput.isPresent()) {
				Long pByWor = pointInput.get().getIndividualPoint();
				ud.setPoints(pByWor);
				this.udRepo.save(ud);
			}
		}
	}
	
	public WellfarePointInput wellfarePointInput() {//현재 입력되어 있는 데이터 출력
		Optional<WellfarePointInput> _pointInput = this.wpiRepo.findById(1);
		if(_pointInput.isPresent()) {
			return _pointInput.get();	
		} else {
			WellfarePointInput pointInput = new WellfarePointInput();
			pointInput.setDepPointPer((long) 0);
			pointInput.setDepPointPlus((long) 0);
			pointInput.setIndividualPoint((long) 0);
			this.wpiRepo.save(pointInput);
			return pointInput;
		}
	}
	//포인트 업데이트
	public void updatePoint(Long depPointPer, Long depPointPlus, Long individualPoint) {
		WellfarePointInput pointInput = this.wpiRepo.findById(1).get();
		pointInput.setDepPointPer(depPointPer);
		pointInput.setDepPointPlus(depPointPlus);
		pointInput.setIndividualPoint(individualPoint);
		this.wpiRepo.save(pointInput);
	}
	
	public List<String> calDepPoint() {//다음 입력시 입력될 포인트 계산
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
	
}
