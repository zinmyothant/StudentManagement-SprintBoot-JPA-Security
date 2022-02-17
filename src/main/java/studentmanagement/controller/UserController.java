package studentmanagement.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import studentmanagement.dto.UserDTO;
import studentmanagement.model.SearchBean;
import studentmanagement.model.UserBean;
import studentmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService uService;

	@Autowired
	BCryptPasswordEncoder encode;

	@ModelAttribute("currentUser")
	public void getCurrentUserInfo(HttpServletRequest session, HttpSession hSession) {
		String currentUserId = session.getRemoteUser();
		List<UserDTO> currentUser = uService.findByIdOrName(currentUserId, "");
		String currentUserName = currentUser.get(0).getName();
		String currentUserPhoto = currentUser.get(0).getImg();
		hSession.setAttribute("userId", currentUserId);
		hSession.setAttribute("userName", currentUserName);
		hSession.setAttribute("userImg", currentUserPhoto);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/home")
	public String home() {

		return "M00001";
	}

	@GetMapping("/userManagement")
	public ModelAndView userManagement(@ModelAttribute("success") String success, ModelMap model) {
		model.addAttribute("success", success);
		return new ModelAndView("USR001", "bean", new SearchBean());
	}

	@GetMapping("/userSearch")
	public String userSearch(@ModelAttribute("bean") SearchBean bean, ModelMap model) {
		UserDTO dto = new UserDTO();
		List<UserDTO> list = new ArrayList<UserDTO>();
		dto.setId(bean.getUserId());
		dto.setName(bean.getUserName());

		if (!dto.getId().equals("") || !dto.getName().equals("")) {
			list = uService.findByIdOrName(dto.getId(), dto.getName());
		} else {
			list = uService.findAll();
		}

		if (list.size() == 0) {
			model.addAttribute("error", "No user found!");
		}
		model.addAttribute("ulist", list);
		return "USR001";
	}

	@GetMapping("/userAdd")
	public ModelAndView userSetup() {
		return new ModelAndView("USR002", "bean", new UserBean());
	}

	@PostMapping("/userAdd")
	public String userAdd(@ModelAttribute("bean") @Validated UserBean bean, BindingResult br, ModelMap model)
			throws IOException {
		if (br.hasErrors()) {
			return "USR002";
		}

		MultipartFile img = bean.getImg();
		UserDTO dto = new UserDTO();
		if (bean.getPassword().equals(bean.getConPwd())) {
			dto.setId(bean.getId());

			List<UserDTO> list = uService.findByIdOrName(dto.getId(), dto.getName());
			if (list.size() != 0) {
				model.addAttribute("error", "User ID already exist!");
				return "USR002";
			} else {
				dto.setName(bean.getName());
				dto.setPassword(bean.getPassword());

				// File Save
				String fileName = img.getOriginalFilename();
				if (fileName.isEmpty()) {
					model.addAttribute("fileError", "Please Choose an Image");
					return "USR002";
				}
				String dir = "./images/" + dto.getId() + "/";
				Path path = Paths.get(dir);
				if (!Files.exists(path)) {
					try {
						Files.createDirectories(path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Path filePath = path.resolve(fileName);
				InputStream inputStream = img.getInputStream();
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

				dto.setImg("/images/" + dto.getId() + "/" + fileName);

				uService.save(dto);
				List<UserDTO> l = uService.findByIdOrName(dto.getId(), dto.getName());
				int i = l.size();
				if (i > 0) {
					model.addAttribute("success", "User Successfully registered");
					return "USR002";
				} else {
					model.addAttribute("error", "User register fail!");
					return "USR002";
				}
			}
		} else {
			model.addAttribute("error", "Password not match!");
			return "USR002";
		}
	}

	@GetMapping("/userUpdate")
	public ModelAndView userUpdateSetup(@RequestParam("id") String id) {
		UserDTO dto = new UserDTO();
		dto.setId(id);
		List<UserDTO> list = uService.findByIdOrName(dto.getId(), dto.getName());
		UserBean bean = new UserBean();
		for (UserDTO res : list) {
			bean.setId(res.getId());
			bean.setName(res.getName());
			bean.setPassword(res.getPassword());
			bean.setConPwd(res.getPassword());
		}
		return new ModelAndView("USR002-01", "bean", bean);
	}

	@PostMapping("/userUpdate")
	public String userUpdate(@ModelAttribute("bean") @Validated UserBean bean, BindingResult br, ModelMap model)
			throws IOException {
		if (br.hasErrors()) {
			return "USR002-01";
		}

		MultipartFile img = bean.getImg();
		UserDTO dto = new UserDTO();
		if (bean.getPassword().equals(bean.getConPwd())) {
			dto.setId(bean.getId());
			dto.setName(bean.getName());
			dto.setPassword(bean.getPassword());

			// File Update
	 		String fileName = img.getOriginalFilename();

			if (!fileName.isEmpty()) {
				String dir = "./images/" + dto.getId() + "/";
				Path delPath = Paths.get("." + uService.findByIdOrName(dto.getId(), "").get(0).getImg());
				Path newPath = Paths.get(dir);
				Path filePath = newPath.resolve(fileName);

				Files.deleteIfExists(delPath);
				InputStream inputStream = img.getInputStream();
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				dto.setImg("/images/" + dto.getId() + "/" + fileName);

			} else {
				dto.setImg(uService.findByIdOrName(dto.getId(), "").get(0).getImg());
			}

			uService.save(dto);
			List<UserDTO> l = uService.findByIdOrName(dto.getId(), dto.getName());
			int i = l.size();
			if (i == 0) {
				model.addAttribute("error", "User update Fail");
				return "USR002-01";
			}
			model.addAttribute("success", "User successfully update");
		} else {
			model.addAttribute("error", "Password not match");
			return "USR002-01";
		}
		return "USR002-01";
	}

	@GetMapping("/userDelete")
	public String userDelete(@RequestParam("id") String id, ModelMap model, RedirectAttributes ra,
			HttpServletRequest session) {
		UserDTO dto = new UserDTO();
		dto.setId(id);
		if (dto.getId().equals(session.getRemoteUser())) {
			ra.addAttribute("success", "Can't delet logined user!");
		} else {
			uService.deleteById(dto.getId());
			List<UserDTO> l = uService.findByIdOrName(dto.getId(), dto.getName());
			int i = l.size();
			if (i == 0) {
				model.addAttribute("error", "User Delete Fail!");
			}
			ra.addAttribute("success", "Delete successful");
		}
		return "redirect:/user/userManagement";
	}

	// Student Reports
	@GetMapping("/userReport")
	public ResponseEntity<byte[]> studentReport() throws Exception, JRException {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(uService.findAll(),false);
		JasperReport compileReport = JasperCompileManager
				.compileReport(new FileInputStream("src/main/resources/reports/user/UserReport_1.jrxml"));

		Map<String, Object> map = new HashMap<>();
		JasperPrint report = JasperFillManager.fillReport(compileReport, map, dataSource);
		byte[] dataPDF = JasperExportManager.exportReportToPdf(report);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=UsersReport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(dataPDF);
	}
	
	  @GetMapping("/report")
	    public void getDocument(HttpServletResponse response) throws IOException, JRException {

//	        String sourceFileName = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "src/main/resources/reports/user/UserReport_1.jrxml")
//	            .getAbsolutePath();
	    	JasperReport sourceFileName = JasperCompileManager
					.compileReport(new FileInputStream("src/main/resources/reports/user/UserReport_1.jrxml"));

	        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(uService.findAll(),false);
	        Map<String, Object> parameters = new HashMap();
	        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
	        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
	        exporter.setConfiguration(reportConfigXLS);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
	        response.setHeader("Content-Disposition", "attachment;filename=UserReport.xlsx");
	        response.setContentType("application/octet-stream");
	        exporter.exportReport();
	    }
	}
