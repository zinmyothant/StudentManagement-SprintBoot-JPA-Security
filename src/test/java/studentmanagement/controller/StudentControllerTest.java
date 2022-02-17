package studentmanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import studentmanagement.dao.StudentRepository;
import studentmanagement.dao.UserRepository;
import studentmanagement.dto.StudentDTO;
import studentmanagement.service.StudentService;
import studentmanagement.service.UserService;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
	StudentDTO student_1=new StudentDTO("S1","Zin Zin","Java Web", "12-1-2021", "Passed");
	StudentDTO student_2=new StudentDTO("S2","Zin Zin","Java Web", "12-1-2021", "Passed");
	StudentDTO student_3=new StudentDTO("S3","Zin Zin","Java Web", "12-1-2021", "Passed");
@Autowired
private MockMvc mockMvc;
@MockBean
private StudentService studentService;
@MockBean
private StudentRepository studentRepository;
@Test
public void studentUpdatePostTest() throws Exception {
	Mockito.when(studentService.save(student_1)).thenReturn(student_1);
	mockMvc.perform(post("/studentUpdate")).andExpect(status().isOk());
}
}
