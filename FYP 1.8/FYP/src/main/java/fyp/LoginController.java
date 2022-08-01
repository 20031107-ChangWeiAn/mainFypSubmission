package fyp;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.bytebuddy.utility.RandomString;

@Controller
public class LoginController {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
    private MemberDetailsService memberService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login2")
	public String login2() {
		return "loginpage2";
	}

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/member/add")
	public String addMember(Model model) {
		model.addAttribute("member", new Member());

		return "add_member";
	}

	@GetMapping("/member/add2")
	public String addMember2(Model model) {
		model.addAttribute("member", new Member());
		return "add_member2";
	}

	@PostMapping("/member/save")
	public String saveMember(HttpServletRequest request, Model model, Member member,
			RedirectAttributes redirectAttribute) {
		Member memberr = memberRepository.findByUsername(member.getUsername());
		Member memberrr = memberRepository.findByEmail(member.getEmail());
		// Member memberrrr =
		// memberRepository.findByContactNumber(member.getContactNumber());

		// if the item was previously added, then we get the quantity that was
		// previously added and increase that
		if (memberr != null || memberrr != null) {
			String error = "Username or email already exists";
			model.addAttribute("error", error); // for html
		} else {
			/*
			 * BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); String
			 * encodedPassword = passwordEncoder.encode(member.getPassword());
			 * 
			 * String contactNumber = ""+ memberrrr;
			 * 
			 * member.setPassword(encodedPassword); member.setContactNumber(contactNumber);
			 */
			
			String pass = getRandomNumberString();
			member.setPassword(pass);

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(member.getPassword());
			member.setPassword(encodedPassword);
			
			member.setRole("ROLE_SELLER");

			memberRepository.save(member);

			String email = request.getParameter("email");
			String otp = pass;
			String token = RandomString.make(30);
			
			try {
	            memberService.updateWebsiteToken(token, email);
	            
	            String link = Utility.getSiteURL(request) + "/localhost:8080/token=" + token;
	            
				sendEmail(email, link, otp);
				model.addAttribute("message", "We have sent a otp to your email. Please check.");
				
			} catch (UnsupportedEncodingException e) {
				model.addAttribute("error", "Error while sending email");
			} catch (MessagingException e) {
				model.addAttribute("error", "Error while sending email");
			} catch (MemberNotFoundException ex) {
	            model.addAttribute("error", ex.getMessage());
	        }

			redirectAttribute.addFlashAttribute("success", "Seller registered!");

//			if (bindingResult.hasErrors()) {
//				return "add_member";
//			}
//			memberRepository.save(member);
		}
		return "redirect:/member";
	}

	@PostMapping("/member/save2")
	public String saveMember2(HttpServletRequest request, Model model, Member member,
			RedirectAttributes redirectAttribute) {
		Member memberr = memberRepository.findByUsername(member.getUsername());
		Member memberrr = memberRepository.findByEmail(member.getEmail());
		// Member memberrrr =
		// memberRepository.findByContactNumber(member.getContactNumber());

		// if the item was previously added, then we get the quantity that was
		// previously added and increase that
		if (memberr != null || memberrr != null) {
			String error = "Username or email already exists";
			model.addAttribute("error", error); // for html
		} else {
			/*
			 * BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); String
			 * encodedPassword = passwordEncoder.encode(member.getPassword());
			 * 
			 * String contactNumber = ""+ memberrrr;
			 * 
			 * member.setPassword(encodedPassword); member.setContactNumber(contactNumber);
			 */
			
			String pass = getRandomNumberString();
			member.setPassword(pass);

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(member.getPassword());
			member.setPassword(encodedPassword);
			
			member.setRole("ROLE_BUYER");

			memberRepository.save(member);

			String email = request.getParameter("email");
			String otp = pass;
			String token = RandomString.make(30);
			
			try {
	            memberService.updateWebsiteToken(token, email);
	            
	            String link = Utility.getSiteURL(request) + "/localhost:8080/token=" + token;
	            
				sendEmail(email, link, otp);
				model.addAttribute("message", "We have sent a otp to your email. Please check.");
				
			} catch (UnsupportedEncodingException e) {
				model.addAttribute("error", "Error while sending email");
			} catch (MessagingException e) {
				model.addAttribute("error", "Error while sending email");
			} catch (MemberNotFoundException ex) {
	            model.addAttribute("error", ex.getMessage());
	        }

			redirectAttribute.addFlashAttribute("success", "Seller registered!");

//			if (bindingResult.hasErrors()) {
//				return "add_member";
//			}
//			memberRepository.save(member);
		}
		return "redirect:/member";
	}

	public void sendEmail(String recipientEmail, String link, String otp) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("contact@worldbay.com", "Worldbay Support");
		helper.setTo(recipientEmail);

		String subject = "Thank you for registering with us!";

		String content = "<p>Welcome to WorldBay!</p>" + "<p>I'm so glad you registered an account with us!</p>"
				+ "<p><a href=\"" + link + "\">Log in to your new account!</a></p>"
				+ "<p>This is your otp password: " + otp + "</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}
	
	// GENERATE RANDOM 6 DIGIT PIN
	  public static String getRandomNumberString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    return String.format("%06d", number);
	  }

}
