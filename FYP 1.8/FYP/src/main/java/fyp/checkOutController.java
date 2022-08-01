package fyp;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class checkOutController {
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private WorldBayRepository itemRepo;
	
	@Autowired
	private deliveryRepository deliveryRepo;
	
	@Autowired
	private voucherRepository voucherRepo;
	
	@Autowired
	private pointsRepository pointsRepo;
	
	@Autowired
	private OrderItemRepository orderRepo;
	
	//for checkout page
	
	@GetMapping("/checkout")
	  public String showCheckout(Model model, Principal principal) {
	    // Get currently logged in user
	    MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
	        .getPrincipal();
	    int loggedInMemberId = loggedInMember.getMember().getId();

	      double loggedInMemberallPay = loggedInMember.getMember().getAllPay();

	      int loggedInMemberallQty = loggedInMember.getMember().getAllQty();
	    // Get shopping cart items added by this user
	    // *Hint: You will need to use the method we added in the CartItemRepository
	    List<CartItem> cartItemList = cartItemRepo.findByMemberId(loggedInMemberId);
	    List<WorldBay> itemList = itemRepo.findAll();
	    
	    //create new arrayList for products
	    List<WorldBay> arrayList = new ArrayList<>(); 
	    
	    double subtotal = 0;
	    
	    //for delivery options
	    List<delivery> deliveryList = deliveryRepo.findAll();
	    
	    if (cartItemList.isEmpty()) {
	      return "cart_empty";
	    } else {
	      
	    // Add the shopping cart items to the model
	    model.addAttribute("cartItemList", cartItemList);
	    
	    // Add the items to the model
	    model.addAttribute("itemList", itemList);

	    // Calculate the total cost of all items in the shopping cart
	    // Initialize the total cost as 0
	    double cartTotal = 0.0;
	    
	    // Initialize the total cost with delivery as 0
	    double cartTotalWithDel = 0.0;
	    
	    // Initialize the total cost with delivery as 0
	    double delFee = 0.0;
	    
	    int quantityCount = 0;
	       
	    double promoAmt = 0.0;

	    // Loop through every item in the cart
	    for (int i = 0; i < cartItemList.size(); i++) {

	      // Gets the row of the first time in the cart
	      CartItem CI = cartItemList.get(i);

	      // Get the quantity
	      int cartQty = CI.getQuantity();

	      // Get the price
	      WorldBay item = CI.getItem();
	      double cartItemPrice = item.getPrice();
	      // double cartItemPrice = CI.getItem().getPrice();
	      // Multiply quantity and price
	      //double subTotal = cartItemPrice * cartQty;
	      subtotal = cartItemPrice * cartQty;
	      CI.setSubtotal(subtotal);
	      
	      // Add to the total cost
	      cartTotal += CI.getSubtotal();
	      
	      //delivery 
	      //cartTotalWithDel += CI.getSubtotal();
	      delFee = CI.getDelivery().getCost();
	          quantityCount += cartQty;
	      
	      if (CI.getVoucher()!=null) {
	        promoAmt = CI.getVoucher().getAmount_off();
	      }
	      
	      model.addAttribute("cartItem", CI);
	      
	    }
	      
	    //subtotal + delivery fee
	    
	      cartTotalWithDel = delFee + cartTotal - promoAmt;
	      
	      if (cartTotalWithDel < 10.0) {
	        model.addAttribute("error","Minimum spending must be above $10");
	      }
	      
	      //int points = loggedInMember.getMember().getPoints();
	      loggedInMemberallPay = loggedInMemberallPay + cartTotalWithDel;
	        loggedInMember.getMember().setAllPay(loggedInMemberallPay);

	        loggedInMemberallQty = loggedInMemberallQty + quantityCount;
	        loggedInMember.getMember().setAllQty(loggedInMemberallQty);

	        memberRepo.save(loggedInMember.getMember());
	    // Add the shopping cart total to the model
	    model.addAttribute("cartTotal", cartTotal);
	    model.addAttribute("cartTotalWithDel", cartTotalWithDel);
	    model.addAttribute("promoAmt", promoAmt);
	    model.addAttribute("delFee", delFee);
	    model.addAttribute("memberId", loggedInMemberId);
	    //Add delivery to the model
	    model.addAttribute("delivery", deliveryList);
	    model.addAttribute("cart",new CartItem());
	    model.addAttribute("cartItemList",cartItemList);
	    
	    return "checkout";
	    }
	  }
	
	
	@PostMapping("/checkout/edit/{id}")
	public String saveUpdatedDelivery(@PathVariable("id") Integer id,@RequestParam(name = "del", required = false) delivery del) {

		// Get cartItem object by cartItem's id
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(id);

		for ( CartItem a: cartItemList) {
			a.setDelivery(del);
			cartItemRepo.save(a);
		}

		return "redirect:/checkout";
	}
	
	@PostMapping("/checkout/promo/{id}")
	public String savePromo(@PathVariable("id") Integer id, @RequestParam(name = "promo") String promo, Model model) {
		// Get cartItem object by cartItem's id
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(id);
		List<member_has_points> listpoints = pointsRepo.findCodeUsedByCodeUsed(promo);
		 LocalDate d2 = LocalDate.now();

		// check if code is still valid after 3 times
				int codeUsed = 0;
				int maxUsage = 0;
				List<OrderItem> orders = orderRepo.findByMemberId(id);
				List<member_has_points> listcodes = pointsRepo.findAllByCodeUsed(promo);
				for (OrderItem o : orders) {
					for (member_has_points mp2 : listcodes) {
							codeUsed = orderRepo.findCountOrderId(promo);
							if (mp2.getCodeUsed().equalsIgnoreCase(promo)) {
								maxUsage = mp2.getVoucher().getMax_redemptions();
								//codeUsed == maxUsage
								if (d2.isAfter(mp2.getExpiryDateOfVoucher()) || codeUsed == maxUsage) {
									mp2.setActive(false);
									pointsRepo.save(mp2);
								
								} else {
									for ( CartItem a: cartItemList) {
										for (member_has_points v : listpoints) {
											if (v.getCodeUsed().equalsIgnoreCase(promo)) {
												if (v.isActive()==true) {
													a.setVoucher(v.getVoucher());
													a.setCodeUsed(promo);
													cartItemRepo.save(a);
												}
											}
										}
									}
								}
							}
					}
				}
		
		
		return "redirect:/checkout";

	}
	
	@PostMapping("/checkout/removeCode/{id}")
	public String deletePromo(@PathVariable("id") Integer id) {
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(id);
		for ( CartItem a: cartItemList) {
			//a.setCodeUsed("");
			//a.setPointsUsed(false);
			a.setVoucher(null);
			cartItemRepo.save(a);
		}
		
		return "redirect:/checkout";

	}		


}
