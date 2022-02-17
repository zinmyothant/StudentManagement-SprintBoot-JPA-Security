package studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import studentmanagement.dao.UserRepository;
import studentmanagement.dto.UserDTO;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	BCryptPasswordEncoder encode;

	public void deleteById(String id) {
		userRepo.deleteById(id);
	}

	public List<UserDTO> findAll() {
		return userRepo.findAll();
	}
	
	public void save(UserDTO dto) {
		String encodePwd = encode.encode(dto.getPassword());
		dto.setPassword(encodePwd);
		dto.setRole("ROLE_USER");
		dto.setEnable(true);
		userRepo.save(dto);
	}

	public List<UserDTO> findByIdOrName(String id, String name) {
		return userRepo.findByIdOrName(id, name);
	}
}
