package com.groupware.note.expense;



import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseDataService {
	private final ExpenseRepository eRepository;
	private final UserDetailsRepository udRepo;
	
	public List<Expense> list(){
		List<Expense> list = eRepository.findAll();
		return list;
	}
	
	public void uploadExpenseData( XSSFSheet worksheet, Files file) throws IOException {
	    for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
	    	  
		      Row row = worksheet.getRow(i);
		      Expense data = new Expense();
		      Optional<UserDetails> _user = udRepo.findByName(row.getCell(0).getStringCellValue());
		      if(_user.isPresent()) {
		    	  UserDetails writer = _user.get();
			      data.setWriter(writer);
			      data.setExpenseType(row.getCell(1).getStringCellValue());
			      data.setAmount(row.getCell(2).getNumericCellValue());
			      data.setUseDate(row.getCell(3).getLocalDateTimeCellValue());
			      data.setDescription(row.getCell(4).getStringCellValue());
			      data.setFile(file);
			      this.eRepository.save(data);
			      System.out.println( i + "체크");
		      } else {
		    	  throw new IOException("엑셀 파일 이름 확인");
		      }
		      
		    }
	}
	
	
	//사진 데이터 업로드, 연동, 다운로드. 이미지1 + 엑셀1 한번에 업로드. 연동. 1개 이미지에 여러개의 영수증이 포함될 수 있음. 화면에 올라간 데이터 띄우고 정렬, 검색기능. 다운로드 가능하게.   
	
}
