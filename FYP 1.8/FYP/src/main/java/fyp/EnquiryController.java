package fyp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EnquiryController {
	
	
	  @Autowired 
	  private EnquiryRepository enquiryRepository;
	  
	  @GetMapping("/view_enquiry") 
	  public String enquiry(Model model) { 
		  List<Enquiry> listEnquiry = enquiryRepository.findAll();
	  	  
		  model.addAttribute("listEnquiry", listEnquiry);
		  return "view_enquiry"; 
	  }
	  
	  @GetMapping("/enquiry/add")
		public String addEnquiry(Model model) {
			model.addAttribute("enquiry", new Enquiry());
			return "enquiry";
		}
	  
	  @PostMapping("/enquiry/save") 
	  public String saveEnquiry(Enquiry enquiry, RedirectAttributes redirectAttribute) {
	  
		  enquiryRepository.save(enquiry);
		  
		  redirectAttribute.addFlashAttribute("success", "Enquiry posted!");
		  return "redirect:/view_enquiry";
	  
	  }
	 
	  @GetMapping("/enquiry/reply/{id}")
		public String editEnquiry(@PathVariable("id") Integer id, Model model) {
			Enquiry enquiry = enquiryRepository.getById(id);
			model.addAttribute("enquiry", enquiry);
			return "edit_enquiry";
		}
	
	  @PostMapping("/enquiry/reply/{id}")
		public String saveUpdatedEnquiry(@PathVariable("id") Integer id, Enquiry enquiry) {
		  enquiryRepository.save(enquiry);
			return "redirect:/view_enquiry";
		}

		@GetMapping("/enquiry/delete/{id}")
		public String deleteEnquiry(@PathVariable("id") Integer id) {
			enquiryRepository.deleteById(id);
			return "redirect:/view_enquiry";
		}
	
}
