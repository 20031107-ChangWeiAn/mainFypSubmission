
package fyp;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleChartsController {

	@Autowired
	private OrderItemRepository orderRepo;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired // tells springboot to look for a class that matches
	private WorldBayRepository worldBayRepo; // CRUD, Repository interface will extend the JpaRepository
												// interface,
												// The JpaRepository will provide methods such as findAll, findById,
												// save, delete, etc

	@GetMapping("/chart")
	public String getPieChart(Model model) {
		Map<String, Integer> graphData = new TreeMap<>();
		List<Member> listMember = memberRepository.findTop5ByOrderByAllPayDesc();
		for (Member a : listMember) {
			graphData.put(a.getName(), a.getAllQty());
		}
		model.addAttribute("chartData", graphData);
		return "google-charts";
	}

	@GetMapping("/chart2")
	public String getPieChart2(Model model) {
		Map<String, Double> graphData = new TreeMap<>();
		List<Member> listMember = memberRepository.findTop5ByOrderByAllPayDesc();
		for (Member a : listMember) {
			graphData.put(a.getName(), a.getAllPay());
		}
		model.addAttribute("chartData", graphData);
		return "google-charts2";
	}

	@GetMapping("/chart3")
	public String getPieChart3(Model model) {
		Map<String, Integer> graphData = new TreeMap<>();
		List<WorldBay> listItems = worldBayRepo.findAll();
		for (WorldBay a : listItems) {
			graphData.put(a.getName(), a.getQuantity());
		}
		model.addAttribute("chartData", graphData);
		return "google-charts3";
	}

	@GetMapping("/chart4")
	public String getPieChart4(Model model) {
		Map<String, Integer> graphData = new TreeMap<>();

		List<WorldBay> listItems = worldBayRepo.findTop2ByOrderByQuantityDesc();

		for (WorldBay a : listItems) {
			graphData.put(a.getName(), a.getQuantity());
		}
		model.addAttribute("chartData", graphData);
		return "google-charts4";
	}

	@GetMapping("/chart5")
	public String getPieChart5(Model model) {
		Map<String, Integer> graphData = new TreeMap<>();
		List<WorldBay> listItems = worldBayRepo.findAll();
		for (WorldBay a : listItems) {
			graphData.put(a.getName(), a.getQuantity());
		}
		model.addAttribute("chartData", graphData);
		return "google-charts5";
	}
}