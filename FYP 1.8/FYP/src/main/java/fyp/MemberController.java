
package fyp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OrderItemRepository orderItemRepo;

	@GetMapping("/member")
	public String viewMember(Model model) {
		List<Member> listMember = memberRepository.findAll();

		model.addAttribute("listMember", listMember);
		return "view_members";
	}

	@GetMapping("/bs")
	public String viewBs(Model model) {
		List<Member> listMember = memberRepository.findAll();

		model.addAttribute("listMember", listMember);
		return "view_bs";
	}

	@GetMapping("/member/{username}")
	public String viewSearchMember(Model model, @PathVariable("username") String username, Member member) {
		List<Member> listMember = memberRepository.findAll();
		for (int i = 0; i < listMember.size(); i++) {
			if (listMember.get(i).getUsername().contains(username)) {
				List<Member> listSearchMember = memberRepository.findSearchByUsername(username);
				model.addAttribute("listSearchMember", listSearchMember);
			}
		}
		return "view_members";
	}

	@GetMapping("/member/add3")
	public String addMember3(Model model) {
		model.addAttribute("member", new Member());
		return "add_member3";
	}

	@PostMapping("/member/save3")
	public String saveMember3(Model model, Member member, RedirectAttributes redirectAttribute) {
		Member memberr = memberRepository.findByUsername(member.getUsername());
		Member memberrr = memberRepository.findByEmail(member.getEmail());

		// if the item was previously added, then we get the quantity that was
		// previously added and increase that
		if (memberr != null || memberrr != null) {
			String error = "Username or email already exists";
			model.addAttribute("error", error); // for html
		} else {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(member.getPassword());

			member.setPassword(encodedPassword);
			member.setRole("ROLE_ADMIN");

			memberRepository.save(member);

			redirectAttribute.addFlashAttribute("success", "ADMIN registered!");

		}
		return "redirect:/member";

	}

	@GetMapping("/member/edit/{id}")
	public String editMember(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepository.getById(id);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		
		model.addAttribute("member", member);
		return "edit_member";
	}

	@PostMapping("/member/edit/{id}")
	public String saveUpdatedMember(@PathVariable("id") Integer id, Member member, Model model) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		model.addAttribute("member", member);
		
		memberRepository.save(member);
		return "redirect:/member";
	}

	@GetMapping("/member/delete/{id}")
	public String deleteMember(@PathVariable("id") Integer id) {
		memberRepository.deleteById(id);
		return "redirect:/member";
	}

	@GetMapping("/tbuyer") public String viewTopBuyer(Model model) {
		List<Member> listMember = memberRepository.findTop5ByOrderByAllPayDesc();
		model.addAttribute("listMember", listMember);
	  
		return "tbuyer";
	}
	 

}
