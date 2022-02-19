package studentmanagement.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import studentmanagement.dao.StudentRepository;
import studentmanagement.dao.UserRepository;
import studentmanagement.dto.StudentDTO;
import studentmanagement.dto.UserDTO;
import studentmanagement.model.StudentBean;
import studentmanagement.service.StudentService;
import studentmanagement.service.UserService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class StudentControllerTest {

	@Autowired
	MockMvc mockMvc;

	@InjectMocks
	private StudentController studentController;
	@Mock
	private StudentService studentService;
	@Mock
	StudentRepository studentRepository;
	@Mock
	StudentDTO StudentDto;

	@org.junit.Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
	}

	StudentDTO student_1 = new StudentDTO("S1", "Zin Zin", "Java Web", "12-1-2021", "Passed");
	StudentDTO student_2 = new StudentDTO("S2", "Zin Zin", "Java Web", "12-1-2021", "Passed");
	StudentDTO student_3 = new StudentDTO("S3", "Zin Zin", "Java Web", "12-1-2021", "Passed");

	@org.junit.Before
	public void student() {
		studentService.save(student_1);
//		studentService.save(student_2);
//		studentService.save(student_3);
		studentRepository.save(student_1);
//		studentRepository.save(student_2);
//		studentRepository.save(student_3);

	}

	// @Disabled("stop")
	@Test
	public void userSearch() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentSearch"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	// @Disabled("stop")
	@Test
	public void studentRegisterSetup() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentRegister"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}
	

	// @Disabled("stop")
	@Test
	public void studentRegister() throws Exception {
		StudentDTO student_1 = new StudentDTO();
		student_1.setStudentId("S1");
		student_1.setStudentName("Zin Zin");
		student_1.setClassName("Java Web");
		student_1.setStatus("Passed");
		student_1.getStatus();
		System.out.println(student_1);
		studentService.save(student_1);
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentRegister"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	// @Disabled("stop")
	@Test
	public void userResult() throws Exception {
		List<StudentDTO> list = new ArrayList<>(Arrays.asList(student_1, student_2, student_3));
		StudentDTO student_1 = new StudentDTO();
		student_1.setStudentId("S1");
		student_1.setStudentName("Zin Zin");
		student_1.setClassName("Java Web");
		student_1.setStatus("Passed");
		student_1.getStatus();

		studentService.save(list.get(1));
		System.out.println(list);
		Mockito.when(studentRepository.findByStudentIdOrStudentNameOrClassName("S1", "Zin Zin", "Java Web"))
				.thenReturn(list);
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentResult"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	// @Disabled("stop") 
	@Test
	public void studentDelete() throws Exception {
		Mockito.when(studentRepository.save(student_1)).thenReturn(student_1);
		Mockito.when(studentRepository.findById(student_1.getStudentId())).thenReturn(Optional.of(student_1));
		studentService.deleteById(student_1.getStudentId());
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentDelete?studentId='S1'"))
				.andExpect(MockMvcResultMatchers.redirectedUrl("/user/studentSearch?success=Delete+successful"));
	}

	@Test
	public void studentUpdate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentUpdate?id='S1'"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void studentUpdateSetup() throws Exception {
		StudentDTO student_1 = new StudentDTO();
		student_1.setStudentId("S1");
		student_1.setStudentName("Zin Zin");
		student_1.setClassName("Java Web");
		student_1.setStatus("Passed");
		student_1.getStatus();
		System.out.println(student_1);
		studentService.save(student_1);
		mockMvc.perform(MockMvcRequestBuilders.post("/user/studentUpdate"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void studentReport() throws Exception {
		List<StudentDTO> list = new ArrayList<StudentDTO>(Arrays.asList(student_1));
		StudentDTO student_1 = new StudentDTO();
		student_1.setStudentId("S1");
		student_1.setStudentName("Zin Zin");
		student_1.setClassName("Java Web");
		student_1.setStatus("Passed");
		student_1.getStatus();
		System.out.println(student_1);
		studentService.save(student_1);
		Mockito.when(studentRepository.findAll()).thenReturn(list);
		mockMvc.perform(MockMvcRequestBuilders.get("/user/studentReport"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
	public void studentReport1() throws Exception {
		List<StudentDTO> list = new ArrayList<StudentDTO>(Arrays.asList(student_1));
		StudentDTO student_1 = new StudentDTO();
		student_1.setStudentId("S1");
		student_1.setStudentName("Zin Zin");
		student_1.setClassName("Java Web");
		student_1.setStatus("Passed");
		student_1.getStatus();
		System.out.println(student_1);
		studentService.save(student_1);
		Mockito.when(studentRepository.findAll()).thenReturn(list);
		mockMvc.perform(MockMvcRequestBuilders.get("/user/reportxlsx"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
	public void studentRegistersetup() throws Exception {
		String y = "2021";
		String m = "2";
		String d = "12";
		StudentDTO student_1 = new StudentDTO();
		student_1.setStudentId("S1");
		student_1.setStudentName("Zin Zin");
		student_1.setRegisterDate(y + "-" + m + "-" + d);
		student_1.setClassName("Java Web");
		student_1.setStatus("Passed");
		student_1.getStatus();
		System.out.println(student_1);
		studentService.save(student_1);
		mockMvc.perform(MockMvcRequestBuilders.post("/user/studentRegister"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}
}
