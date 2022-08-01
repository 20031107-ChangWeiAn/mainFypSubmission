
package fyp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	public List<OrderItem> findByMemberId(int id);

	public List<OrderItem> findByOrderId(String id);

	@Query("SELECT DISTINCT o.orderId FROM OrderItem o WHERE member_id = :id")
	public List<String> findDistinctByMemberId(int id);
	
	@Query("SELECT DISTINCT o.orderId FROM OrderItem o")
	public List<String> findDistinct();
	
	@Query("SELECT COUNT(DISTINCT o.orderId) FROM OrderItem o WHERE code_used = :code")
	public int findCountOrderId(String code);
}
