package com.groupware.note.expense;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsRepository;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseDataService {
	private final ExpenseRepository eRepository;
	private final UserDetailsRepository udRepo;
	//기본
	public List<Expense> list(){
		List<Expense> list = this.eRepository.findOrderByUseDate();
		return list;
	}
	
	//날짜 설정
	public List<Expense> dateSetList(LocalDateTime startDate, LocalDateTime endDate ){
		List<Expense> list = this.eRepository.findBetDate(startDate, endDate);
		return list;
	}
	
	//위쪽에 검색창 통해 부서/이름 검색	
	public List<Expense> barSearchedList(String browse, String search ){
		
		switch(search) {
		case "WRITER" :
			List<Expense> list1 = this.eRepository.findByNameOrderByDate("%" + browse+ "%");
			return list1;
		case "DEPARTMENT":
			List<Expense> list2 = this.eRepository.findByDepOrderByDate("%" + browse+ "%");
			return list2;
		}
		return null;
	}
	//이름 클릭시 실행
	public List<Expense> nameList(String st){
		List<Expense> list = this.eRepository.findByEmailOrderByDate(st);
		return list;
	}
	
	//부서 클릭시 실행
	public List<Expense> depList(String st){
		List<Expense> list = this.eRepository.findByDepOrderByDateandName(st);
		return list;
	}
	
	//날짜 클릭시 실행
	public List<Expense> dateList(LocalDateTime dt){
		List<Expense> list = this.eRepository.findByDateOrderBy(dt);
		return list;
	}
	
	//데이터 업로드
	public void uploadExpenseData( XSSFSheet worksheet, Files file, Users user) {
	    for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
		      Row row = worksheet.getRow(i);
		      if(row.getCell(0).getStringCellValue().equals("End")) {
		    	  break;
		      }
		      Expense data = new Expense();
		      Optional<UserDetails> _user = udRepo.findById(user.getUserId());
		      if(_user.isPresent()) {
		    	  UserDetails writer = _user.get();
			      data.setWriter(writer);
			      data.setExpenseType(row.getCell(0).getStringCellValue());
			      data.setAmount(row.getCell(1).getNumericCellValue());
			      data.setUseDate(row.getCell(2).getLocalDateTimeCellValue());
			      data.setDescription(row.getCell(3).getStringCellValue());
			      data.setFile(file);
			      this.eRepository.save(data);
		      }
	    }
	}
	public void deleteExpense(UserDetails user) {//0827 박은영 null처리
		List<Expense> list = this.eRepository.findByWriter(user);
		if(!list.isEmpty() || list.isEmpty()) {
			for(Expense expense : list) {
				expense.setWriter(null);
				this.eRepository.save(expense);	
			}
		}else {throw new DataNotFoundException("데이터가 없습니다");}
		}
}