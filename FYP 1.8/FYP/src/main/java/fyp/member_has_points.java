package fyp;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class member_has_points {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String actions;
	
	private int pointsUpdatedBy;
	
	private LocalDate transactionDate;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "voucher_code")
	private voucher voucher;
	
	private String codeUsed;

	// Whether the promotion code is currently active. A promotion code is only
		// active if the coupon is also valid.
	private boolean active;
	
	private LocalDate expiryDateOfVoucher;

	public int getPointsUpdatedBy() {
		return pointsUpdatedBy;
	}
	

	public void setPointsUpdatedBy(int pointsUpdatedBy) {
		this.pointsUpdatedBy = pointsUpdatedBy;
	}

	/*
	 * public Date getTransactionDate() { return transactionDate; }
	 * 
	 * public void setTransactionDate(Date transactionDate) { this.transactionDate =
	 * transactionDate; }
	 */

	public Member getMember() {
		return member;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}


	public void setMember(Member member) {
		this.member = member;
	}


	public voucher getVoucher() {
		return voucher;
	}


	public void setVoucher(voucher voucher) {
		this.voucher = voucher;
	}
	

	public LocalDate getExpiryDateOfVoucher() {
		return expiryDateOfVoucher;
	}


	public void setExpiryDateOfVoucher(LocalDate expiryDateOfVoucher) {
		this.expiryDateOfVoucher = expiryDateOfVoucher.plusMonths(6);
	}


	public int getId() {
		return id;
	}


	public String getActions() {
		return actions;
	}


	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getCodeUsed() {
		return codeUsed;
	}


	public void setCodeUsed(String codeUsed) {
		this.codeUsed = codeUsed;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	

}
