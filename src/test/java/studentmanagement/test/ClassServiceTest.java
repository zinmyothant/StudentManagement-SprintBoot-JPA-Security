package studentmanagement.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import studentmanagement.dao.ClassRepository;
import studentmanagement.dto.ClassDTO;
import studentmanagement.service.ClassService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ClassServiceTest {

	@InjectMocks
	private ClassService classService;
	@Mock
	private ClassRepository repo;
	
//	@Test
//	public void saveTest() {
//		ClassDto dto=new ClassDto();
//		dto.setId("C001");
//		dto.setName("Zin Zin");
//		assertEquals(dto.toString(), classService.save(dto).toString());
//	}
//	
	@Test
	public void save() {
		ClassDTO dto=new ClassDTO();
		dto.setId("C1");
		dto.setName("Java");
		classService.save(dto);
		verify(repo,times(1)).save(dto);
	} 
	@Test
	public void select() {
		//ClassDTO dto=new ClassDTO();
		ClassDTO dto=new ClassDTO();
		dto.setId("C1");
		dto.setName("Java");
		classService.save(dto);
		classService.findAll();
		verify(repo,times(1)).findAll();
				
	}
	@Test
	public  void findidandname() {
		ClassDTO dto=new ClassDTO();
		dto.setId("C1");
		dto.setName("Java");
		classService.findByIdOrName("C1","Java");
		verify(repo,times(1)).findByIdOrName("C1","Java");
	}
	
}
