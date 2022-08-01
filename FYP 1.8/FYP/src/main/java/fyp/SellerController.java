package fyp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerController {
	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/seller")
	public String viewMember(Model model, HttpServletRequest request) {
		/*
		 * List<Member> listMember = memberRepository.findAll(); Member memberr =
		 * memberRepository.findByRole(member.getRole());
		 * 
		 * if (memberr.getRole().contains("ROLE_SELLER")) {
		 * model.addAttribute("listMember", listMember); }
		 */
		List<Member> listMember = memberRepository.findAll();		
		model.addAttribute("listMember", listMember);
		
		return "view_sellers";
	}
}
