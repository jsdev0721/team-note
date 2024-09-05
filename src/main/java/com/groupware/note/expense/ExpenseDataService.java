package com.groupware.note.expense;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

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
	public List<Expense> nameList(Integer userId){
		List<Expense> list = this.eRepository.findByUserIdOrderByDate(userId);
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
	
	//비목 클릭시 실행
	public List<Expense> expenseTypeList(String et){
		List<Expense> list = this.eRepository.findByExpenseTypeOrderByDate(et);
		return list;
	}
	
	//데이터 업로드
	public void uploadExpenseData( XSSFSheet worksheet, Files file, Users user) {
		Optional<UserDetails> _user = udRepo.findById(user.getUserId());
		LocalDateTime d = null;
		if(_user.isPresent()) {
			for(int i = 7; i <30; i++) {
				Row row = worksheet.getRow(i);
				if(row.getCell(18).getCellType()==CellType.BLANK) {
					break;
				}
				Expense eData = new Expense();
				if(row.getCell(2).getCellType()!=CellType.BLANK) {
				d = row.getCell(2).getLocalDateTimeCellValue();
				}
				eData.setWriter(_user.get());
				eData.setUseDate(d);
				eData.setAccount(row.getCell(6).getStringCellValue());
				eData.setBreakDown(row.getCell(11).getStringCellValue());
				eData.setAmount(row.getCell(18).getNumericCellValue());
				eData.setExpenseType(row.getCell(24).getStringCellValue());
				eData.setDescription(row.getCell(30).getStringCellValue());
				eData.setFile(file);
				this.eRepository.save(eData);
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
		}
	}
}