package studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studentmanagement.dao.StudentRepository;
import studentmanagement.dto.StudentDTO;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepo;
	

	public void deleteById(String id) {
		studentRepo.deleteById(id);
	}

	public List<StudentDTO> findAll() {
		return studentRepo.findAll();
	}

	public List<StudentDTO> findByStudentIdOrStudentNameOrClassName(String studentId, String studentName,
			String className) {
		return studentRepo.findByStudentIdOrStudentNameOrClassName(studentId, studentName, className);
	}
	
	//save has return to test with junit
	public StudentDTO save(StudentDTO dto) {
		return studentRepo.save(dto);
	}
}
