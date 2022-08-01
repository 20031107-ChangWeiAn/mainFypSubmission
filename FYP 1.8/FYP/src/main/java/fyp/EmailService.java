package fyp;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
	
	  private final TemplateEngine templateEngine;

	    private final JavaMailSender javaMailSender;

	    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
	        this.templateEngine = templateEngine;
	        this.javaMailSender = javaMailSender;
	    }

	    public String sendMail(Member member) throws MessagingException {
	        Context context = new Context();
	        context.setVariable("member", member);

	        String process = templateEngine.process("welcome", context);
	        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
	        helper.setSubject("Welcome " + member.getName());
	        helper.setText(process, true);
	        helper.setTo(member.getEmail());
	        javaMailSender.send(mimeMessage);
	        return "Sent";
	    }
    

}
