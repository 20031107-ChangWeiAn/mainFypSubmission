package fyp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class ChatbotController {
	@GetMapping("/chatbot") 
	public String Chatbot() {
		return "chatbot";
	}
	

	
}
