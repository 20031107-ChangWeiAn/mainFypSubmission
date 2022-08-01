
package fyp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	public Member findByUsername(String username);
	public List<Member> findSearchByUsername(String username);
	@Query("SELECT m FROM Member m WHERE m.email = ?1")
    public Member findByEmail(String email); 
     
    public Member findByResetPasswordToken(String token);
    
	public List<Member> findById(int id);
	
	public List<Member> findTop5ByOrderByAllPayDesc();
	
	//public Member findByContactNumber(String contactNumber);
    
    @Query(value = "Select * from member where deleted=0",nativeQuery = true)
    List<Member> getUndeletedProfile();

    @Query(value = "Select * from member where deleted=1", nativeQuery = true)
    List<Member> getDeletedProfile();
	
    public Member findByWebsiteToken(String token);
}
