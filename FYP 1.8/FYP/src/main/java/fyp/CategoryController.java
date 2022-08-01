
package fyp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // define the controller and allows Springboot to automatically detect the
			// classes
public class CategoryController {

	@Autowired // tells springboot to look for a class that matches
	private CategoryRepository categoryRepository; // CRUD, Repository interface will extend the JpaRepository
													// interface, The JpaRepository will provide methods such as
													// findAll, findById, save, delete, etc

	@GetMapping("/categories") // get requests onto specific handler methods
	public String viewCategories(Model model) { // link the controller to the view, represents the data and data logic,
												// provides the data to be displayed
		List<Category> listCategories = categoryRepository.findAll();

		model.addAttribute("listCategories", listCategories);
		return "view_categories";
	}

	@GetMapping("/categories/add")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		return "add_category";
	}

	@PostMapping("/categories/save") // post request (forms)
	public String saveCategory(Category category) {
		categoryRepository.save(category);
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model) { // template variables in the request URI
																				// mapping
		Category category = categoryRepository.getById(id);
		model.addAttribute("category", category);
		return "edit_category";
	}

	@PostMapping("/categories/edit/{id}")
	public String saveUpdatedCategory(@PathVariable("id") Integer id, Category category) {
		categoryRepository.save(category);
		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		categoryRepository.deleteById(id);
		return "redirect:/categories";
	}

}
