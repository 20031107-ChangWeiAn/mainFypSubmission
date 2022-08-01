package fyp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.validation.Valid;

import ch.qos.logback.classic.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller // define the controller and allows Springboot to automatically detect the
// classes
public class ItemController {

	@Autowired // tells springboot to look for a class that matches
	private WorldBayRepository worldBayRepository; // CRUD, Repository interface will extend the JpaRepository interface,
	// The JpaRepository will provide methods such as findAll, findById,
	// save, delete, etc

	@Autowired // tells springboot to look for a class that matches
	private CategoryRepository categoryRepository;// CRUD, Repository interface will extend the JpaRepository interface,
	// The JpaRepository will provide methods such as findAll, findById,
	// save, delete, etc
	@Autowired
	private ItemReviewRepository itemReviewRepository;

	@GetMapping("/items") // get requests onto specific handler methods
	public String viewCategories(Model model) { // link the controller to the view, represents the data and data logic,
		// provides the data to be displayed
		List<WorldBay> listXtreme = worldBayRepository.getUndeletedItem();

		model.addAttribute("listXtreme", listXtreme);
		return "view_items";
	}

	@GetMapping("/items/add")
	public String addXtreme(Model model) {
		model.addAttribute("xtreme", new WorldBay());
		List<Category> catList = categoryRepository.findAll();
		model.addAttribute("catList", catList);
		return "add_item";
	}

	@GetMapping("/items/deletedItem")
	public String viewDeleteItem(Model model) { // link the controller to the view, represents the data and data logic,
		// provides the data to be displayed
		List<WorldBay> listXtreme = worldBayRepository.getDeletedItem();

		model.addAttribute("listXtreme", listXtreme);
		return "deleted_item";
	}

	@PostMapping("/items/save") // post request (forms)
	public String saveItems(@Valid WorldBay item, BindingResult bindingResult,
							@RequestParam("itemImage") MultipartFile imgFile) { // get the filename of the
		// image uploaded
		if (bindingResult.hasErrors()) {
			return "add_item";
		}
		String imageName = imgFile.getOriginalFilename();
		// set the filename to the item object
		item.setImages(imageName);
		item.setDeleted(0); // Just added

		WorldBay savedItem = worldBayRepository.save(item);

		try {
			// create a directory to upload
			String uploadDir = "uploads/items/" + savedItem.getId();
			// directory path is /uploads/items/{id}
			Path uploadPath = Paths.get(uploadDir);
			// check if the directory path exists, if it does not exist
			// create the directory path
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			// copy the file to the directory path
			Path fileToCreatePath = uploadPath.resolve(imageName);
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException io) {
			// if it fails throw and exception
			io.printStackTrace();
		}
		// upload the file to my local directory
		return "redirect:/items";
	}
	@GetMapping("/items/{id}")
	public String viewSingleItem(@PathVariable("id") Integer id, Model model) { // model is for controller to
		// communicate with the html
		// get the item by id from the path variable
		WorldBay item = worldBayRepository.findById(id).get();
		List<ItemReview> itemReviews = itemReviewRepository.findByItemId(id.toString());
		SingleItemDAO singleItemDAO = new SingleItemDAO();
		singleItemDAO.setId(item.getId());
		singleItemDAO.setName(item.getName());
		singleItemDAO.setDescription(item.getDescription());
		singleItemDAO.setPrice(item.getPrice());
		singleItemDAO.setQuantity(item.getQuantity());
		singleItemDAO.setSize(item.getSize());
		singleItemDAO.setImages(item.getImages());
		singleItemDAO.setDeleted(item.getDeleted());
		singleItemDAO.setItemsReview(itemReviews);

		model.addAttribute("item", singleItemDAO);
		return "view_single_item";
	}

	@GetMapping("/items/edit/{id}")
	public String editItem(@PathVariable("id") Integer id, Model model) {
		WorldBay item = worldBayRepository.getById(id);
		model.addAttribute("item", item);
		List<Category> catList = categoryRepository.findAll();
		model.addAttribute("catList", catList);
		return "edit_item";
	}

	@PostMapping("/items/edit/{id}")
	public String saveUpdatedCategory(@PathVariable("id") Integer id, WorldBay item,
									  @RequestParam("itemImage") MultipartFile imgFile) {
		String imageName = imgFile.getOriginalFilename();

		if (imageName.isEmpty()) {
			imageName = worldBayRepository.getById(id).getImages();
			item.setImages(imageName);
			item.setDeleted(0);
			worldBayRepository.save(item);

		} else {
			item.setImages(imageName);
			item.setDeleted(0);
			WorldBay savedItem = worldBayRepository.save(item);

			try {
				String uploadDirectory = "uploads/items/" + savedItem.getId();
				Path uploadPath = Paths.get(uploadDirectory);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Path fileToCreatepath = uploadPath.resolve(imageName);
				Files.copy(imgFile.getInputStream(), fileToCreatepath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException io) {
				io.printStackTrace();
			}
		}

		return "redirect:/items";
	}

	@GetMapping("/items/delete2/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		worldBayRepository.deleteById(id);
		return "redirect:/items";
	}

	@GetMapping("/items/delete/{id}")
	public String deleteCategory2(@PathVariable("id") Integer id) {
		WorldBay item = worldBayRepository.getById(id);
		item.setDeleted(1);
		worldBayRepository.save(item);
		return "redirect:/items";
	}
	@GetMapping("/items/recover/{id}")
	public String recoverItem(@PathVariable("id") Integer id) {
		WorldBay item = worldBayRepository.getById(id);
		item.setDeleted(0);
		worldBayRepository.save(item);
		return "redirect:/items";
	}

	@PostMapping("/items/add_review/{itemId}")
	public String SaveItemReview(@PathVariable(name="itemId") String itemId ,
								 @RequestParam(name="rating") String rating, @RequestParam(name="review") String review){
		ItemReview itemReview = new ItemReview();
		itemReview.setItemID(itemId);
		itemReview.setReview(review);
		itemReview.setRating(rating);
		itemReviewRepository.save(itemReview);
		return "redirect:/index";
	}
}