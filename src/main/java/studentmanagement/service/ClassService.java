package studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studentmanagement.dao.ClassRepository;
import studentmanagement.dto.ClassDTO;

@Service
public class ClassService {
	
	@Autowired
	private ClassRepository classRepo;

	public List<ClassDTO> findAll() {
		return classRepo.findAll();
	}

	public List<ClassDTO> findByIdOrName(String id, String name) {
		return classRepo.findByIdOrName(id, name);
	}

	public void save(ClassDTO dto) {
		classRepo.save(dto);
	}
	
}
