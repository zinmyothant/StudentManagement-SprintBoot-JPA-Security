package studentmanagement.test;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import studentmanagement.dao.UserRepository;
import studentmanagement.dto.UserDTO;
import studentmanagement.service.UserService;



@SpringBootTest
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository repo;

	@Test
	public void save() {
		UserDTO user=new UserDTO();
		user.setId("1");
		user.setName("zin zin");
		user.setPassword("123");
		
		userService.save(user);
		verify(repo,times(1)).save(user);
	}
//	@Test
//	public void saveTest() {
//		UserDto user = new UserDto();
//		user.setId("1");
//		user.setName("zin zin");
//		user.setPassword("123");
//		user.setConfirm("123");
//		assertEquals(user.toString(), userService.save(user).toString());
//	}
	@Test
	public void selectAll() {
		userService.findAll();
		verify(repo,times(1)).findAll();
	}
//	@Test
//	public void selectAllTest() {
//		assertEquals(0, userService.getAllUser().size());
//	}
	
	@Test
	public void deleteTest() {
		UserDTO user=new UserDTO(); 
		user.setId("1");
		user.setName("Admin");
		userService.deleteById(user.getId());
		verify(repo,times(1)).deleteById(user.getId());
	}
	
	@Test
	public void findnameorid() {
		UserDTO user=new UserDTO(); 
		user.setId("1");
		user.setName("Admin");
		userService.findByIdOrName("1", "Admin");
		verify(repo,times(1)).findByIdOrName("1", "Admin");
	}

}
