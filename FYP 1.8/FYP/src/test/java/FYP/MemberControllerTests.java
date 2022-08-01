package FYP;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fyp.Member;
import fyp.MemberController;
import fyp.MemberRepository;
import fyp.WorldBayController;
import fyp.WorldBayRepository;

@WebMvcTest(MemberController.class)
public class MemberControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean 
	private MemberRepository memberRepository;
	
	@Test
	public void testSavedMember() {
		Member newMember = new Member();
		newMember.setId(0);
		newMember.setName("Jenn");
		newMember.setUsername("j");
		newMember.setPassword("123");
		newMember.setEmail("j@gmail.com");
		newMember.setContactNumber("81234567");
		newMember.setRole("Admin");
		newMember.setResetPasswordToken(null);
		newMember.setImages(null);
		newMember.setDeleted(0);
		newMember.setWebsiteToken(null);
		newMember.setAllPay(0);
		newMember.setAllQty(0);
		
		Member savedMember = new Member();
		savedMember.setId(0);
		
	}

}
