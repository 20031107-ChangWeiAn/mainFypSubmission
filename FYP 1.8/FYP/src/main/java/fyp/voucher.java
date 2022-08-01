package fyp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class voucher {

	@Id
	private String code;

	private String name;

	private int pointsRequired;

	private double amount_off;

	// Maximum number of times this promotion code can be redeemed.
	private int max_redemptions;

	//public int getId() {
		//return id;
	//}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPointsRequired() {
		return pointsRequired;
	}

	public double getAmount_off() {
		return amount_off;
	}

	public void setAmount_off(double amount_off) {
		this.amount_off = amount_off;
	}

	public void setAmount_off(int amount_off) {
		this.amount_off = amount_off;
	}

	public int getMax_redemptions() {
		return max_redemptions;
	}

	public void setMax_redemptions(int max_redemptions) {
		this.max_redemptions = max_redemptions;
	}

	/*
	 * public Date getExpires_at() { return expires_at; }
	 * 
	 * public void setExpires_at(Date expires_at) { this.expires_at = expires_at; }
	 */

}
