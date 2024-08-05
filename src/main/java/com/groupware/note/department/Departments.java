package com.groupware.note.department;

import java.util.List;

import com.groupware.note.position.Positions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Departments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer departmentId;
	
	private String departmentName;
	
	private long points;
	
	@OneToMany(mappedBy = "department")
	private List<Positions> positions;
	
}
