package com.groupware.note.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

}
