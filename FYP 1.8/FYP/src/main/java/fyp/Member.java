
package fyp;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotEmpty(message = "Member name cannot be empty!")
	@Size(min = 3, max = 100, message = "Member length must be between 3 and 100 characters!")
	private String name;

	@NotNull
	@NotEmpty(message = "Member username cannot be empty!")
	@Size(min = 3, max = 100, message = "Member length must be between 3 and 100 characters!")
	private String username;

	@NotNull
	@NotEmpty(message = "Password cannot be empty!")
	@Size(min = 3, max = 100, message = "Password must be between 3 and 100 characters!")
	private String password;

	@NotNull
	@NotEmpty(message = "Email cannot be empty!")
	@Size(min = 3, max = 100, message = "Email must be between 3 and 100 characters!")
	private String email;

	private String contactNumber;

	private String role;

	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	@OneToMany(mappedBy = "member")
	private Set<CartItem> cartItems;

	private String images;

	private Integer deleted;

	@Column(name = "website_token")
	private String websiteToken;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getWebsiteToken() {
		return websiteToken;
	}

	public void setWebsiteToken(String websiteToken) {
		this.websiteToken = websiteToken;
	}

	private int allQty = 0;

	public int getAllQty() {
		return allQty;
	}

	public void setAllQty(int allQty) {
		this.allQty = allQty;
	}

	private double allPay = 0;

	public double getAllPay() {
		return allPay;
	}

	public void setAllPay(double allPay) {
		this.allPay = allPay;
	}

}
