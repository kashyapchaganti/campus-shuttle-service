package com.campusshuttle.campusshuttleservice.repository;

import com.campusshuttle.campusshuttleservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsBySUID(long suid);

    Student findBySUID(Long suid);
}
