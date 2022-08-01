
package fyp;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MemberDetails implements UserDetails {
    
	private Member member;
	

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public MemberDetails(Member member) {
		this.member = member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRole());
		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	public String getName() {
		return member.getName();
	}
	
	@Override
	public String getUsername() {
		return member.getUsername();
	}
	
	public String getEmail() {
		return member.getEmail();
	}
	
	public String getContactNumber() {
		return member.getContactNumber();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
}
