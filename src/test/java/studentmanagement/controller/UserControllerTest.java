package studentmanagement.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import studentmanagement.dao.StudentRepository;
import studentmanagement.dao.UserRepository;
import studentmanagement.dto.UserDTO;
import studentmanagement.model.UserBean;
import studentmanagement.service.UserService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {
	@Autowired
	MockMvc mockMvc;
	@InjectMocks
	private UserController userController;
	@Mock
	private UserService uService;
	@Mock
	private UserRepository userRepository;
	@Mock
	UserDTO UserDTO;
	@Mock
	UserBean bean;
	@org.junit.Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}
	@Before
	public void classservice() {
		UserDTO user=new UserDTO();
		user.setId("s1");
		user.setName("zin");
		user.setPassword("123");
		uService.save(user);
		userRepository.save(user);
	}
	UserDTO user_1=new UserDTO("1","Zin","123");
	UserDTO user_2=new UserDTO("2","Myo","123");
	UserDTO user_3=new UserDTO("3","Thant","123");

	@Before
	public void student() {
				uService.save(user_1);
		uService.save(user_2);
		uService.save(user_3);
		userRepository.save(user_1);
		userRepository.save(user_2);
		userRepository.save(user_3);
		
	}
	@Test
	public void home() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/home")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void userManagement() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/userManagement"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void userAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/userAdd")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void userAddSetup() throws Exception {
		UserDTO user = new UserDTO();
		user.setId("U1");
		user.setName("Admin");
		user.setPassword("123");
		uService.save(user);
		mockMvc.perform(MockMvcRequestBuilders.post("/user/userAdd")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void userUpdate() throws Exception {
		
		bean.setPassword("123");
		bean.setConPwd("123");
		UserDTO user = new UserDTO();
		user.setId("U1");
		user.setName("Admin");
		user.setPassword("123");
		uService.save(user);
		userRepository.save(user);
		uService.findByIdOrName("U1", "Admin");
		userRepository.findByIdOrName(user.getId(), user.getName());
		//bean.getPassword();
		mockMvc.perform(MockMvcRequestBuilders.post("/user/userUpdate"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
	public void userSearch() throws Exception {
		List<UserDTO> list=new ArrayList<UserDTO>(Arrays.asList(user_1));
	Mockito.when(userRepository.findByIdOrName(user_1.getId(), "Zin")).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/user/userSearch?id='1'"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void userReport() throws Exception {
		List<UserDTO> list=new ArrayList<UserDTO>(Arrays.asList(user_1));
	Mockito.when(userRepository.findAll()).thenReturn(list);
	//	uService.findAll();
		mockMvc.perform(MockMvcRequestBuilders.get("/user/userReport"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
	public void userReport2() throws Exception {
		List<UserDTO> list=new ArrayList<UserDTO>(Arrays.asList(user_1));
	Mockito.when(userRepository.findAll()).thenReturn(list);
	//	uService.findAll();
		mockMvc.perform(MockMvcRequestBuilders.get("/user/report"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test 
	public void userDelete() throws Exception {
		Mockito.when(userRepository.save(user_1)).thenReturn(user_1);
		userRepository.deleteById(user_1.getId());
	
		mockMvc.perform(MockMvcRequestBuilders.get("/user/userDelete?id='S1'"))
		.andExpect(MockMvcResultMatchers.redirectedUrl("/user/userManagement?success=Delete+successful"));
	}

	@Test
	public void userUpdateSet() throws Exception {
		
		UserDTO user=new UserDTO();
		user.setId("S1");
		user.setName("Zin Zin");
		uService.findByIdOrName("S1", "Zin Zin");
		UserDTO user1=new UserDTO();
		user1.setId("S1"); 
		user1.setName("Zin");
		uService.save(user1);

		mockMvc.perform(MockMvcRequestBuilders.get("/user/userUpdate?id='S1'")).andExpect(MockMvcResultMatchers.status().isOk());

	}
}
