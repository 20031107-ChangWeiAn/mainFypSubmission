
package fyp;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity // class maps to a database table
public class WorldBay {
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // PK will auto increment
	private int id;

	@NotNull
	@NotEmpty(message = "Item name cannot be empty!")
	@Size(min = 5, max = 50, message = "Item name length must be between 5 and 50 characters!")
	private String name;

	@NotNull
	@NotEmpty(message = "Description cannot be empty!")
	@Size(min = 5, max = 100, message = "Description length must be between 5 and 100 characters!")
	private String description;

	@NotNull
	@DecimalMin(value = "0.00", inclusive = false, message = "Price cannot be empty!")
	private double price;

	@NotNull
	@Min(value = 0, message = "Quantity name cannot be empty!")
	private int quantity;

	private String size;

	private String images;

	private Integer deleted;
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false) // joining tables
	@NotNull
	private Category category;

	@OneToMany(mappedBy = "item")
	private Set<CartItem> cartItems;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
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
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the images
	 */
	public String getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(String images) {
		this.images = images;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}


}
