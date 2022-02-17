package studentmanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import studentmanagement.dto.ClassDTO;

@Repository
public interface ClassRepository extends JpaRepository<ClassDTO, String> {
	List<ClassDTO> findByIdOrName(String id, String name);
}
