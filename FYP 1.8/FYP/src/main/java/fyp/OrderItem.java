
package fyp;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;


@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String orderId;

	private String transactionId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private WorldBay item;
	
	@ManyToOne
	@JoinColumn(name = "delivery_id")
	private delivery delivery;

	private int quantity;

	@Transient
	private double subtotal;
	
	private LocalDate Orderdate;
	
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "voucher_code")
	private voucher voucher;
	
	private String codeUsed;
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * @return the item
	 */
	public WorldBay getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(WorldBay item) {
		this.item = item;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the subtotal
	 */
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public LocalDate getOrderdate() {
		return Orderdate;
	}

	public void setOrderdate(LocalDate orderdate) {
		Orderdate = orderdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(delivery delivery) {
		this.delivery = delivery;
	}

	public voucher getVoucher() {
		return voucher;
	}

	public void setVoucher(voucher voucher) {
		this.voucher = voucher;
	}
	
	public String getCodeUsed() {
		return codeUsed;
	}

	public void setCodeUsed(String codeUsed) {
		this.codeUsed = codeUsed;
	}
	
	
}
