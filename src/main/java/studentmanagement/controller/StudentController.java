package studentmanagement.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import studentmanagement.dto.ClassDTO;
import studentmanagement.dto.StudentDTO;
import studentmanagement.model.SearchBean;
import studentmanagement.model.StudentBean;
import studentmanagement.service.ClassService;
import studentmanagement.service.StudentService;

@Controller
@RequestMapping("/user")
public class StudentController {

	@Autowired
	StudentService sService;

	@Autowired
	ClassService cService;
	
//	@ModelAttribute("clist")
//	public List<String> classList() {
//		List<ClassDTO> clist = cService.findAll();
//		List<String> l = new ArrayList<>();
//		for (int i = 0; i < clist.size(); i++) {
//			l.add(clist.get(i).getName());
//		}
//		return l;
//	}

	@GetMapping("/studentSearch")
	public ModelAndView userSearchSetUp(@ModelAttribute("success") String success, ModelMap model) {
		model.addAttribute("success", success);
		return new ModelAndView("BUD001", "bean", new SearchBean());
	}

	@GetMapping("/studentResult")
	public String userSearch(@ModelAttribute("bean") SearchBean bean, ModelMap model) {
		StudentDTO dto = new StudentDTO();
		List<StudentDTO> list = new ArrayList<StudentDTO>();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());
		//for test
		dto.setStudentId("123");
		dto.setStudentName("Zin");
		dto.setClassName("Java Web");
		if (!dto.getStudentId().equals("") || !dto.getStudentName().equals("") || !dto.getClassName().equals("")) {
			list = sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
		} else {
			list = sService.findAll();
		}

		if (list.size() == 0) {
			model.addAttribute("error", "No Student found!");
		}
		model.addAttribute("list", list);
		return "BUD001";
	}

	@GetMapping("/studentUpdate") 
	public ModelAndView studentUpdateSetUp(@RequestParam("id") String id, ModelMap model) {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId(id);
	List<StudentDTO> list =new ArrayList();
			list=sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
		StudentBean bean = new StudentBean();
		for (StudentDTO res : list) {
			bean.setStudentId(res.getStudentId());
			bean.setStudentName(res.getStudentName());
			bean.setClassName(res.getClassName());
			bean.setStatus(res.getStatus());
			String[] dt = res.getRegisterDate().toString().split("-");
			bean.setYear(dt[0]);
			bean.setMonth(dt[1]);
			bean.setDay(dt[2]);
		}
		return new ModelAndView("BUD002-01", "bean", bean);
	}

	@PostMapping("/studentUpdate")
	public String studentUpdate(@ModelAttribute("bean") @Validated StudentBean bean, BindingResult br, ModelMap model) {
//		if (br.hasErrors() || bean.getYear().equals("Year") || bean.getMonth().equals("Month")
//				|| bean.getDay().equals("Day")) {
//			model.addAttribute("error", "Register date can't be blank!");
//			return "BUD002-01";
//		}

		String y = bean.getYear();
		String m = bean.getMonth();
		String d = bean.getDay();

		StudentDTO dto = new StudentDTO();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());
		dto.setRegisterDate(y + "-" + m + "-" + d);
		dto.setStatus(bean.getStatus());
		sService.save(dto);
//		List<StudentDTO> l = sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
//		int i = l.size();
//		if (i == 0) {
//			model.addAttribute("error", "Student update Fail");
//			return "BUD002-01";
//		}
		model.addAttribute("success", "Student successfully updated");
		return "BUD002-01";
	}

	@GetMapping("/studentRegister")
	public ModelAndView studentRegisterSetup() {
		return new ModelAndView("BUD002", "bean", new StudentBean());
	}

	@PostMapping("/studentRegister")
	public String studentRegister(@ModelAttribute("bean") @Validated StudentBean bean, BindingResult br, ModelMap model,
			RedirectAttributes ra) {
//		if (br.hasErrors() || bean.getYear().equals("Year") || bean.getMonth().equals("Month")
//				|| bean.getDay().equals("Day")) {
//			model.addAttribute("error", "Register date can't be blank!");
//			return "BUD002";
//		}

		String y = bean.getYear();
		String m = bean.getMonth();
		String d = bean.getDay();

		StudentDTO dto = new StudentDTO();
		dto.setStudentId(bean.getStudentId());
		List<StudentDTO> list = sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
		if (list.size() != 0) {
			model.addAttribute("error", "StudentID already exist!");
			return "BUD002";
		} else { 
			dto.setStudentName(bean.getStudentName());
			dto.setClassName(bean.getClassName());
			dto.setRegisterDate(y + "-" + m + "-" + d);
			dto.setStatus(bean.getStatus());
			sService.save(dto); 
			List<StudentDTO> l = sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
			int i = l.size(); 
			if (i > 0) {
				model.addAttribute("success", "Student successfully registered");
//				return "redirect:/studentRegister";
				return "BUD002";
			} else {
				model.addAttribute("error", "Student register fail!");
				return "BUD002";
			}
		}
	}

	@GetMapping("/studentDelete")
	public String studentDelete(@PathParam("id") String id, ModelMap model, RedirectAttributes ra) {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId(id);
		sService.deleteById(dto.getStudentId());
//		List<StudentDTO> l = sService.findByStudentIdOrStudentNameOrClassName(dto.getStudentId(), dto.getStudentName(), dto.getClassName());
//		int i = l.size();
//		if (i > 0) {
			ra.addAttribute("success", "Delete successful");
		//} else {
			//ra.addAttribute("error", "Delete Fail!");
		//}
		return "redirect:/user/studentSearch";
	}
	
	//Student Reports
	@GetMapping("/studentReport")
	public ResponseEntity<byte[]> studentReport() throws Exception, JRException {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sService.findAll());
		JasperReport compileReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/reports/student/StudentReport.jrxml"));
		
		Map<String, Object> map = new HashMap<>();
		JasperPrint report = JasperFillManager.fillReport(compileReport, map, dataSource);
		byte[] data = JasperExportManager.exportReportToPdf(report);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=StudentsReport.pdf");
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}
	  @GetMapping("/reportxlsx")
	    public void getDocument(HttpServletResponse response) throws IOException, JRException {

//	        String sourceFileName = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "src/main/resources/reports/user/UserReport_1.jrxml")
//	            .getAbsolutePath();
	    	JasperReport sourceFileName = JasperCompileManager
					.compileReport(new FileInputStream("src/main/resources/reports/student/StudentReport.jrxml"));

	        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(sService.findAll(),false);
	        Map<String, Object> parameters = new HashMap();
	        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
	        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
	        exporter.setConfiguration(reportConfigXLS);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
	        response.setHeader("Content-Disposition", "attachment;filename=StudentReport.xlsx");
	        response.setContentType("application/octet-stream");
	        exporter.exportReport();
	    }
}
