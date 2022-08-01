package fyp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {
	
	@Autowired
	private MemberDetailsService memberService;
	 
	@Autowired
	private MemberRepository memberRepository;
	
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		// Get currently logged in user
	    MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    int loggedInMemberId = loggedInMember.getMember().getId();
	    model.addAttribute("memberId", loggedInMemberId);
		
	    List<Member> listMember = memberRepository.findAll();
		
		model.addAttribute("listMember", listMember);
		return "profile";
	}
	
	@PostMapping("/profile/save/{id}") // post request (forms)
	public String saveProfile(@Valid Member member, BindingResult bindingResult,
							@RequestParam("profileImage") MultipartFile imgFile) { // get the filename of the
		
		// image uploaded
		if (bindingResult.hasErrors()) {
			return "profile";
		}
		String profileImage = imgFile.getOriginalFilename();
		// set the filename to the profile object
		member.setImages(profileImage);
		member.setDeleted(0); // Just added

		Member savedProfile = memberRepository.save(member);

		try {
			// create a directory to upload
			String uploadDir = "uploads/profile/" + savedProfile.getId();
			// directory path is /uploads/profile/{id}
			Path uploadPath = Paths.get(uploadDir);
			// check if the directory path exists, if it does not exist
			// create the directory path
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			// copy the file to the directory path
			Path fileToCreatePath = uploadPath.resolve(profileImage);
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException io) {
			// if it fails throw and exception
			io.printStackTrace();
		}
		// upload the file to my local directory
		return "redirect:/profile";
	}

	@GetMapping("/profile/edit/{id}")
	public String editProfile(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepository.getById(id);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		
		model.addAttribute("member", member);
			
		return "edit_profile";
	}

	@PostMapping("/profile/edit/{id}")
	public String saveUpdatedProfile(@PathVariable("id") Integer id, Member member, Model model
									 /* @RequestParam("profileImage") MultipartFile imgFile*/) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		
		model.addAttribute("member", member);
		memberRepository.save(member);
		/*
		 * String imageName = imgFile.getOriginalFilename();
		 * 
		 * if (imageName.isEmpty()) { imageName =
		 * memberRepository.getById(id).getImages(); member.setImages(imageName);
		 * memberRepository.save(member);
		 * 
		 * } else { member.setImages(imageName); Member savedProfile =
		 * memberRepository.save(member);
		 * 
		 * try { String uploadDirectory = "uploads/profile/" + savedProfile.getId();
		 * Path uploadPath = Paths.get(uploadDirectory); if (!Files.exists(uploadPath))
		 * { Files.createDirectories(uploadPath); }
		 * 
		 * Path fileToCreatepath = uploadPath.resolve(imageName);
		 * Files.copy(imgFile.getInputStream(), fileToCreatepath,
		 * StandardCopyOption.REPLACE_EXISTING); } catch (IOException io) {
		 * io.printStackTrace(); } }
		 */

		return "redirect:/profile";
	}

}
