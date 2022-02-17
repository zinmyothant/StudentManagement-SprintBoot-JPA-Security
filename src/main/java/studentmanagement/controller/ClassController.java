package studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import studentmanagement.dto.ClassDTO;
import studentmanagement.model.ClassBean;
import studentmanagement.service.ClassService;

@Controller
@RequestMapping("/user")
public class ClassController {

	@Autowired
	ClassService cService;

	@GetMapping("/classRegister")
	public ModelAndView classRegisterSetup() {
		return new ModelAndView("BUD003", "bean", new ClassBean());
	}

	@PostMapping("/classRegister")
	public String classRegister(@ModelAttribute("bean") @Validated ClassBean bean, BindingResult br, ModelMap model) {
		if (br.hasErrors()) {
			return "BUD003";
		}

		ClassDTO dto = new ClassDTO();
		dto.setId(bean.getId());
		

		List<ClassDTO> list = cService.findByIdOrName(dto.getId(), dto.getName());
		if (list.size() != 0) {
			model.addAttribute("error", "ClassID already exist!");
			return "BUD003";
		} else {
			dto.setName(bean.getName());
			cService.save(dto);
			List<ClassDTO> l = cService.findByIdOrName(dto.getId(), dto.getName());
			int i = l.size();
			if (i > 0) {
				model.addAttribute("success", "Class Successfully Registered");
				return "BUD003";
			} else {
				model.addAttribute("error", "Class Register Fail!");
				return "BUD003";
			}
		}
	}
}
