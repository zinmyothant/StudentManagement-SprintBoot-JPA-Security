package studentmanagement.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import studentmanagement.dao.ClassRepository;
import studentmanagement.dto.ClassDTO;
import studentmanagement.service.ClassService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ClassControllerTest {
	@Autowired
	MockMvc mockMvc;
	@InjectMocks
	private ClassController classController;
	@InjectMocks
	private ClassService classService;
	@Mock
	private ClassRepository classRepository;
	@org.junit.Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(classController).build();
	} 
	@Test
	public void classRegister() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/classRegister")).andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	@Test 
	public void classRegisterSetup() throws Exception {
		ClassDTO classDto=new ClassDTO();
		classDto.setId("C1");
		classDto.setName("Java");
		classService.save(classDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/user/classRegister")).andExpect(MockMvcResultMatchers.status().isOk());
		
	}

	
}
