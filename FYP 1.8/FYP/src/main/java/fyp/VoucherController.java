package fyp;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VoucherController {

	@Autowired
	private pointsRepository pointsRepo;

	@Autowired
	private voucherRepository voucherRepo;


	@GetMapping("/voucher")
	public String showVoucher(Model model, Principal principal) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		// member_has_points checkPoints =
		// pointsRepo.findDistinctMemberIdByMemberId(loggedInMemberId);
		List<member_has_points> listpoints = pointsRepo.findAllByMemberId(loggedInMemberId);
		int points = 0;

		for (member_has_points mp : listpoints) {
			points += mp.getPointsUpdatedBy();
		}
		
		

		// model.add
		model.addAttribute("points", points);
		model.addAttribute("listpoints", listpoints);
		model.addAttribute("m", loggedInMember);

		return "vouchers";
	}

	@PostMapping("/voucher/save")
	public String getVoucher(Model model, Principal principal, @RequestParam(name = "voucher") String voucher) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		// member_has_points checkPoints =
		// pointsRepo.findDistinctMemberIdByMemberId(loggedInMemberId);
		Member member = loggedInMember.getMember();
		int points = pointsRepo.SumpointsUpdatedByByMemberId(loggedInMemberId);
		member_has_points memberPoints = new member_has_points();
		LocalDate d2 = LocalDate.now();
		List<voucher> voucherList = voucherRepo.findAll();
		String error = "";
		// String code = "";

		for (voucher v : voucherList) {

			if (v.getCode().equalsIgnoreCase(voucher)) {

				if (points >= 50) {
					memberPoints.setMember(member);
					memberPoints.setVoucher(v);
					memberPoints.setPointsUpdatedBy(-v.getPointsRequired());
					memberPoints.setTransactionDate(d2);
					memberPoints.setActions("REDEEM " + v.getName() + " VOUCHER");
					memberPoints.setExpiryDateOfVoucher(d2);
					memberPoints.setCodeUsed(getAlphaNumericString(5));
					memberPoints.setActive(true); // code = v.getCode();
					pointsRepo.save(memberPoints);

				}

			}

			else if (v.getCode().equalsIgnoreCase(voucher)) {
				if (points >= 100) {
					memberPoints.setMember(member);
					memberPoints.setVoucher(v);
					memberPoints.setPointsUpdatedBy(-v.getPointsRequired());
					memberPoints.setTransactionDate(d2);
					memberPoints.setActions("REDEEM " + v.getName() + " VOUCHER");
					memberPoints.setCodeUsed(getAlphaNumericString(5));
					memberPoints.setActive(true);
					pointsRepo.save(memberPoints);

				}
			}

		}
		model.addAttribute("error", error);
		model.addAttribute("voucherList", voucherList);

		return "redirect:/voucher";
	}
	

	static String getAlphaNumericString(int n) {

		// lower limit for LowerCase Letters
		int lowerLimit = 97;

		// lower limit for LowerCase Letters
		int upperLimit = 122;

		Random random = new Random();

		// Create a StringBuffer to store the result
		StringBuffer r = new StringBuffer(n);

		for (int i = 0; i < n; i++) {

			// take a random value between 97 and 122
			int nextRandomChar = lowerLimit + (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));

			// append a character at the end of bs
			r.append((char) nextRandomChar);
		}

		// return the resultant string
		return r.toString();
	}

}
