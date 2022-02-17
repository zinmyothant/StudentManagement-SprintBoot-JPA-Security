package studentmanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import studentmanagement.dto.StudentDTO;

@Repository
public interface StudentRepository extends JpaRepository<StudentDTO, String> {

	List<StudentDTO> findByStudentIdOrStudentNameOrClassName(String studentId, String studentName, String className);
}
