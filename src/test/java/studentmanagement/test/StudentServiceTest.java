package studentmanagement.test;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import studentmanagement.dao.StudentRepository;
import studentmanagement.dto.StudentDTO;
import studentmanagement.service.StudentService;

@SpringBootTest
public class StudentServiceTest {

	@Autowired
	private StudentService sService;
	
	@MockBean
	private StudentRepository studentRepository;
	@Disabled("Disabled util!")
	@Test
	public void testSave() {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId("ST001");
		dto.setStudentName("Student Test");
		dto.setClassName("ClassTest");
		dto.setRegisterDate("2020-2-15");
		dto.setStatus("Failed");
		Assertions.assertEquals(dto.toString(), sService.save(dto).toString());
	}
	
	@Test
	public void testFindAll() {
		Assertions.assertEquals(1, sService.findAll().size());
	}
	
	@Test
	public void testFindByStudentIdOrStudentNameOrClassName() {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId("ST001");
		dto.setStudentName("Student Test");
		dto.setClassName("ClassTest");
		dto.setRegisterDate("2020-2-15");
		dto.setStatus("Failed");
		sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
		//Assertions.assertEquals(dto.toString(), sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName()).toString());

	verify(studentRepository,times(1)).findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(),dto.getClassName());
	
	}
}
