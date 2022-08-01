
package fyp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller // define the controller and allows Springboot to automatically detect the
			// classes
public class WorldBayController {

	@Autowired // tells springboot to look for a class that matches
	private WorldBayRepository worldBayRepository; // CRUD, Repository interface will extend the JpaRepository interface,
												// The JpaRepository will provide methods such as findAll, findById,
												// save, delete, etc
	@GetMapping("/index")
	public String index(Model model) { // link the controller to the view, represents the data and data logic,
		// provides the data to be displayed
List<WorldBay> listXtreme = worldBayRepository.getUndeletedItem();

model.addAttribute("listXtreme", listXtreme);
return "index";
}

	@GetMapping("/aboutuspage")
	public String aboutuspage() {
		return "aboutuspage";
	}

	@GetMapping("/tnc")
	public String tnc() {
		return "tnc";
	}

	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
	@GetMapping("/faq")
	public String faq() {
		return "faq";
	}
	
	@GetMapping("/titems")
	public String viewTopItems(Model model) {

		List<WorldBay> listItems = worldBayRepository.findTop2ByOrderByQuantityDesc();
		model.addAttribute("listItems", listItems);
	
		return "titems";

	}
}
