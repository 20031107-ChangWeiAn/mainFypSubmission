package fyp;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberDetailsService implements UserDetailsService {

	@Autowired
	private MemberRepository memberRepository;

	@Override
	public MemberDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// finds the username in the database
		Member member = memberRepository.findByUsername(username);

		// when username does not exist
		// if null then throw exception
		if (member == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		// when username exist
		// else create a new object memberdetails
		// with the member information obtained
		return new MemberDetails(member);
	}
	
	public void updateResetPasswordToken(String token, String email) throws MemberNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
        	member.setResetPasswordToken(token);
            memberRepository.save(member);
        } else {
            throw new MemberNotFoundException("Could not find any member with the email " + email);
        }
    }
     
    public Member getByResetPasswordToken(String token) {
        return memberRepository.findByResetPasswordToken(token);
    }
     
    public void updatePassword(Member member, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);
         
        member.setResetPasswordToken(null);
        memberRepository.save(member);
    }
    
    public void updateWebsiteToken(String token, String email) throws MemberNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
        	member.setWebsiteToken(token);
            memberRepository.save(member);
        } else {
            throw new MemberNotFoundException("Could not find any member with the email " + email);
        }
    }
     
    public Member getByWebsiteToken(String token) {
        return memberRepository.findByWebsiteToken(token);
    }
}
