
package fyp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

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

	@ManyToOne
	@JoinColumn(name = "voucher_code")
	private voucher voucher;

	private String codeUsed;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public WorldBay getItem() {
		return item;
	}

	public void setItem(WorldBay item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public int getId() {
		return id;
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
