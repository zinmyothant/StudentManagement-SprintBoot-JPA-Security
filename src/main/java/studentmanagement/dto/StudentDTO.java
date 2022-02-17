package studentmanagement.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class StudentDTO {

	@Id
	private String studentId;
	
	private String studentName, className, registerDate, status;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public StudentDTO(String studentId, String studentName, String className, String registerDate, String status) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
		this.className = className;
		this.registerDate = registerDate;
		this.status = status;
	}

	public StudentDTO() {
		super();
	}

	@Override
	public String toString() {
		return "StudentDTO [studentId=" + studentId + ", studentName=" + studentName + ", className=" + className
				+ ", registerDate=" + registerDate + ", status=" + status + "]";
	}
	
	
}
