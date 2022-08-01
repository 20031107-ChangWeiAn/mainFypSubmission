package fyp;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WishListController {

	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private WishListRepository wishListRepo;

	@Autowired
	private WorldBayRepository itemRepo;

	@Autowired
	private MemberRepository memberRepo;

	@GetMapping("/wishlist")
	public String showWishList(Model model, Principal principal) {
		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the wishListRepository
		List<WishList> WishList = wishListRepo.findByMemberId(loggedInMemberId);

		// Add the shopping cart items to the model
		model.addAttribute("WishList", WishList);

		// Calculate the total cost of all items in the shopping cart
		// Initialize the total cost as 0
		double cartTotal = 0.0;

		// Loop through every item in the cart
		for (int i = 0; i < WishList.size(); i++) {

			// Gets the row of the first time in the cart
			WishList CI = WishList.get(i);

			// Get the quantity
			int cartQty = CI.getQuantity();

			// Get the price
			WorldBay item = CI.getItem();
			double WishListPrice = item.getPrice();
			// double WishListPrice = CI.getItem().getPrice();
			// Multiply quantity and price
			double subTotal = WishListPrice * cartQty;
			CI.setSubtotal(subTotal);

			// Add to the total cost
			cartTotal += CI.getSubtotal();
			
			int itemId = CI.getItem().getId();
			model.addAttribute("itemId", itemId);
			model.addAttribute("qty", cartQty);
		}

		// Add the shopping cart total to the model
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("memberId", loggedInMemberId);
		return "wishlist";
	}

	@PostMapping("/wishlist/add/{itemId}")
	public String addToWishList(@PathVariable("itemId") int itemId, @RequestParam("quantity") int quantity,
			Principal principal) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Check in the wishListRepo if item was previously added by user.
		// *Hint: we will need to write a new method in the wishListRepository
		WishList WishList = wishListRepo.findByMemberIdAndItemId(loggedInMemberId, itemId);

		// if the item was previously added, then we get the quantity that was
		// previously added and increase that
		if (WishList != null) {

			int currentQuantity = WishList.getQuantity();
			WishList.setQuantity(currentQuantity + quantity);
			wishListRepo.save(WishList);
		} else {

			// if the item was NOT previously added,
			// then prepare the item and member objects
			WorldBay item = itemRepo.getById(itemId);
			Member member = memberRepo.getById(loggedInMemberId);

			// Create a new WishList object
			WishList newCI = new WishList();

			// Set the item and member as well as the new quantity in the new WishList
			// object
			newCI.setItem(item);
			newCI.setMember(member);
			newCI.setQuantity(quantity);

			// Save the new WishList object to the repository
			wishListRepo.save(newCI);
		}

		return "redirect:/wishlist";

	}

	@PostMapping("/wishlist/update/{id}")
	public String updateWishList(@PathVariable("id") int WishListId, @RequestParam("qty") int qty) {

		// Get WishList object by WishList's id
		WishList WishList = wishListRepo.getById(WishListId);

		// Set the quantity of the carItem object retrieved
		WishList.setQuantity(qty);

		// Save the WishList back to the wishListRepo
		wishListRepo.save(WishList);

		return "redirect:/wishlist";
	}

	@GetMapping("/wishlist/remove/{id}")
	public String removeFromWishList(@PathVariable("id") int WishListId) {

		// Remove item from wishListRepo
		wishListRepo.deleteById(WishListId);
		return "redirect:/wishlist";
	}
	
	
	
	

}