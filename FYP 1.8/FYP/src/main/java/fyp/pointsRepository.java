package fyp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface pointsRepository extends JpaRepository<member_has_points, Integer> {
	
	@Query("SELECT SUM(m.pointsUpdatedBy) FROM member_has_points m WHERE member_id = :id")
	public int SumpointsUpdatedByByMemberId(int id);
	
	//public int findByMemberId(int id);
	
	// check if item was previously added by user
	//public member_has_points findDistinctMemberId(int memberId);
	
	public List<member_has_points> findAllByMemberId(int id);
	
	public List<member_has_points> findCodeUsedByCodeUsed(String code);
	
	public List<member_has_points> findAllByCodeUsed(String code);



}
