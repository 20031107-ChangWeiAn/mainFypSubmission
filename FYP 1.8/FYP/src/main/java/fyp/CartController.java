
package fyp;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@Service
public class CartController {

	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private WorldBayRepository itemRepo;

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private OrderItemRepository orderItemRepo;

	@Autowired
	private pointsRepository pointsRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private deliveryRepository deliveryRepo;

	@GetMapping("/cart")
	public String showCart(Model model, Principal principal) {
		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(loggedInMemberId);
		List<WorldBay> itemList = itemRepo.findAll();

		// create new arrayList for products
		List<WorldBay> arrayList = new ArrayList<>();

		double subtotal = 0;

		// for delivery options
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
				// double subTotal = cartItemPrice * cartQty;
				subtotal = cartItemPrice * cartQty;
				CI.setSubtotal(subtotal);

				// Add to the total cost
				cartTotal += CI.getSubtotal();

				// delivery
				cartTotalWithDel += CI.getDelivery().getCost();
			}

			// check if item is not in cartItem
			for (CartItem a : cartItemList) {
				for (WorldBay b : itemList) {
					if (a.getItem().getId() != b.getId()) {
						arrayList.add(b);
					}
				}
			}

			Collections.shuffle(arrayList);

			// add the recommendation list to the model
			model.addAttribute("arrayList", arrayList);
			// Add the shopping cart total to the model
			model.addAttribute("cartTotal", cartTotal);
			model.addAttribute("cartTotalWithDel", cartTotalWithDel);
			model.addAttribute("memberId", loggedInMemberId);
			// Add delivery to the model
			model.addAttribute("delivery", deliveryList);

			return "cart";
		}
	}

	@PostMapping("/cart/add/{itemId}")
	public String addToCart(@PathVariable("itemId") int itemId, @RequestParam("quantity") int quantity,
			Principal principal) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Check in the cartItemRepo if item was previously added by user.
		// *Hint: we will need to write a new method in the CartItemRepository
		CartItem cartItem = cartItemRepo.findByMemberIdAndItemId(loggedInMemberId, itemId);

		// if the item was previously added, then we get the quantity that was
		// previously added and increase that
		if (cartItem != null) {
			int currentQuantity = cartItem.getQuantity();
			cartItem.setQuantity(currentQuantity + quantity);
			cartItemRepo.save(cartItem);
		} else {

			// if the item was NOT previously added,
			// then prepare the item and member objects
			WorldBay item = itemRepo.getById(itemId);
			Member member = memberRepo.getById(loggedInMemberId);

			// Create a new CartItem object
			CartItem newCI = new CartItem();

			// Set the item and member as well as the new quantity in the new CartItem
			// object
			newCI.setItem(item);
			newCI.setMember(member);
			newCI.setQuantity(quantity);
			newCI.setDelivery(deliveryRepo.getById(1));
			newCI.setVoucher(null);

			cartItemRepo.save(newCI);
		}

		return "redirect:/cart";

	}

	@PostMapping("/cart/update/{id}")
	public String updateCart(@PathVariable("id") int cartItemId, @RequestParam("qty") int qty) {

		// Get cartItem object by cartItem's id
		CartItem cartItem = cartItemRepo.getById(cartItemId);

		// Set the quantity of the carItem object retrieved
		cartItem.setQuantity(qty);

		// Save the cartItem back to the cartItemRepo
		cartItemRepo.save(cartItem);

		return "redirect:/cart";
	}

	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable("id") int cartItemId) {

		// Remove item from cartItemRepo
		cartItemRepo.deleteById(cartItemId);
		return "redirect:/cart";
	}

	@PostMapping("/checkout/process_order")
	public String processOrder(Model model, @RequestParam("formcartTotal") double cartTotal,
			@RequestParam("memberId") int memberId, @RequestParam("orderId") String orderId,
			@RequestParam("transactionId") String transactionId) throws MessagingException {
		// Retrieve cart items purchased
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(memberId);
		List<OrderItem> OrderItemList = orderItemRepo.findByMemberId(memberId);

		//Date d2 = new Date();
		 LocalDate d2 = LocalDate.now();

		// Get member object
		Member member = memberRepo.getById(memberId);

		// get memberPoints object
		member_has_points points = new member_has_points();
		member_has_points points2 = new member_has_points();

		// Loop to iterate through all cart items
		for (int i = 0; i < cartItemList.size(); i++) {
			// Retrieve details about current cart item
			CartItem currentCartItem = cartItemList.get(i);
			int qtyPurchased = currentCartItem.getQuantity();
			// Update item table
			// Inventory has to be updated (items)
			WorldBay inventoryItem = currentCartItem.getItem();
			int inventoryQty = inventoryItem.getQuantity();
			int remainingQty = inventoryQty - qtyPurchased;
			// Update the remaining qty to the inventory
			inventoryItem.setQuantity(remainingQty);
			// Save it to the database
			itemRepo.save(inventoryItem);
			// Add item to order table
			OrderItem newOrder = new OrderItem();
			newOrder.setItem(inventoryItem);
			newOrder.setMember(member);
			newOrder.setOrderId(orderId);
			newOrder.setQuantity(qtyPurchased);
			newOrder.setSubtotal(currentCartItem.getSubtotal());
			newOrder.setTransactionId(transactionId);
			// newOrder.setPoints((int)cartTotal);
			newOrder.setOrderdate(d2);
			newOrder.setStatus("Processing Stock");
			newOrder.setDelivery(currentCartItem.getDelivery());
			// reduce points and update points

			if (currentCartItem.getVoucher() != null) {
				newOrder.setVoucher(currentCartItem.getVoucher());
				newOrder.setCodeUsed(currentCartItem.getCodeUsed());
			}
			points.setMember(member);
			points.setPointsUpdatedBy((int) cartTotal);
			points.setTransactionDate(d2);
			points.setActions("FROM ORDER PURCHASE");
			// update tables
			orderItemRepo.save(newOrder);
			pointsRepo.save(points);

			// clear cart items belonging to member
			cartItemRepo.deleteById(currentCartItem.getId());

		}

		// Pass info to view to display success page
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("member", member);
		model.addAttribute("orderId", orderId);
		model.addAttribute("transactionId", transactionId);
		model.addAttribute("date", d2);
		model.addAttribute("orderItemList", OrderItemList);

		// Send email
		String subject = "WorldBay order is confirmed!";
		String body = "Thank you for your order!\n" + "Order ID: " + orderId + "\n" + "Transaction ID: " + transactionId
				+ "\n" + "Items Ordered:";
		for (int b = 0; b < cartItemList.size(); b++) {
			CartItem items = cartItemList.get(b);
			double subTotal = items.getQuantity() * items.getItem().getPrice();
			items.setSubtotal(subTotal);
			body += "\n" + items.getItem().getName() + "  x" + items.getQuantity() + "\nSubTotal:$"
					+ items.getSubtotal();
			body += "/templates/success.html";

		}
		String to = member.getEmail();
		// sendEmail(to, subject, body);
		
		//get value for discount
		double discount = 0;
		//get value for delivery fee
		double delCost=0;
				
		for (CartItem ci : cartItemList) {
			
			if (ci.getVoucher()!=null) {
				discount = ci.getVoucher().getAmount_off();
			}
			
			delCost = ci.getDelivery().getCost();
			
		}

		Context context = new Context();
		context.setVariable("member", member);
		context.setVariable("cartTotal", cartTotal);
		context.setVariable("cartItemList", cartItemList);
		context.setVariable("orderId", orderId);
		context.setVariable("discount", discount);
		context.setVariable("delCost", delCost);
		context.setVariable("transactionId", transactionId);
		context.setVariable("date", d2);

		String process = templateEngine.process("invoiceEmail", context);
		javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setSubject("WorldBay order is confirmed!");
		helper.setText(process, true);
		helper.setTo(member.getEmail());

		ClassPathResource resource = new ClassPathResource("/static/images/logo.jpg");

		helper.addInline("logoImage", resource);
		javaMailSender.send(mimeMessage);

		for (int a = 0; a < cartItemList.size(); a++) {
			CartItem userCartItem = cartItemList.get(a);
			double subTotal = userCartItem.getQuantity() * userCartItem.getItem().getPrice();
			userCartItem.setSubtotal(subTotal);
		}

		return "success";
	}

	@GetMapping("/history")
	public String viewHistory(Model model) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		List<String> listOrderItem = orderItemRepo.findDistinctByMemberId(loggedInMemberId);
		List<OrderItem> orders = orderItemRepo.findByMemberId(loggedInMemberId);
		double totalCost = 0;
		double delFee = 0;
		double voucherCode = 0;
		double subTotal = 0;
		
		for (OrderItem o : orders) {
			delFee = o.getDelivery().getCost();
			if (o.getVoucher()!=null) {
				voucherCode = o.getVoucher().getAmount_off();
			}
			subTotal = o.getItem().getPrice()*o.getQuantity();
		}
		
		totalCost = (delFee+subTotal)-voucherCode;
		model.addAttribute("orders", orders);
		model.addAttribute("listOrderItem", listOrderItem);
		model.addAttribute("totalCost",totalCost);
		model.addAttribute("v",voucherCode);

		return "orders_history";

	}

	@GetMapping("/track/{id}")
	public String trackOrder(@PathVariable("id") String id, Model model) {
		List<OrderItem> listOrderItem = orderItemRepo.findByOrderId(id);
		double totalCost = 0;
		double delFee = 0;
		double voucherCode = 0;
		double subTotal = 0;
		String status = "";
		LocalDate date = null;
		String name = "";
		
		for (OrderItem o : listOrderItem) {
			delFee = o.getDelivery().getCost();
			if (o.getVoucher()!=null) {
				voucherCode = o.getVoucher().getAmount_off();
			}
			subTotal = o.getItem().getPrice()*o.getQuantity();
			status = o.getStatus();
			date = o.getOrderdate();
			name = o.getMember().getName();
		}
		
		totalCost = (delFee+subTotal)-voucherCode;
		
		model.addAttribute("totalCost", totalCost);
		model.addAttribute("status",status);
		model.addAttribute("listOrderItem", listOrderItem);
		model.addAttribute("orderId", id);
		model.addAttribute("vCode", voucherCode);
		model.addAttribute("subTotal", subTotal);
		model.addAttribute("date", date);
		model.addAttribute("delCost", delFee);
		model.addAttribute("name", name);
		
		return "single_order_detail";
	}

	@GetMapping("/ordersSeller") // get requests onto specific handler methods
	public String Vieworders(Model model) {
		List<OrderItem> listOrderItem = orderItemRepo.findAll();
		double voucherCode = 0;
		
		for (OrderItem o : listOrderItem) {
			if (o.getVoucher()!=null) {
				voucherCode = o.getVoucher().getAmount_off();
			}
		}
		
		model.addAttribute("v", voucherCode);
		model.addAttribute("listOrderItem", listOrderItem);
		return "orderTrackB";
	}
	
	@GetMapping("/UpdateOrder/{id}")
	public String updateOrders(@PathVariable("id") String id, Model model) {
		
		List<OrderItem> listOrderItem = orderItemRepo.findByOrderId(id);
		double voucherCode = 0;
		String name = "";
		
		for (OrderItem o : listOrderItem) {
			if (o.getVoucher()!=null) {
				voucherCode = o.getVoucher().getAmount_off();
			}
			name = o.getMember().getName();
		}
		
		model.addAttribute("v", voucherCode);
		model.addAttribute("listOrderItem",listOrderItem);
		model.addAttribute("id",id);
		model.addAttribute("name",name);

		return "edit_order";

	}
	
	@PostMapping("/UpdateOrder/{id}")
	public String SaveUpdateOrders(@PathVariable("id") String id, Model model,@RequestParam(name = "status") String status) {
		List<OrderItem> listOrderItem = orderItemRepo.findByOrderId(id);
		for (OrderItem o : listOrderItem) {
			o.setStatus(status);
			orderItemRepo.save(o);
		}
		
		return "redirect:/ordersSeller";

	}

	


}