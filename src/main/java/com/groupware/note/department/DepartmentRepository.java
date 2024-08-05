package com.groupware.note.department;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Departments, Integer> {
	Optional<Departments> findByDepartmentName(String departmentName);
}
