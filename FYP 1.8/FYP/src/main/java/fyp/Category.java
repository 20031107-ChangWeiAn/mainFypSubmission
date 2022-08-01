
package fyp;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity // class maps to a database table
public class Category {
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // PK will auto increment
	private int id;

	@NotNull
	@NotEmpty(message = "Category name cannot be empty!")
	@Size(min = 3, max = 10, message = "Category length must be between 3 and 10 characters!")
	private String name;

	@OneToMany(mappedBy = "category")
	private Set<WorldBay> worldBay;

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
}
