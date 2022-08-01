
package fyp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	// SELECT * FROM CartItem where member_id = id;
	// get the list of cartitems based on member id
	public List<CartItem> findByMemberId(int id);

	// check if item was previously added by user
	public CartItem findByMemberIdAndItemId(int memberId, int itemId);
	
	// check if item was previously added by user
	//public CartItem findByMemberId2(int memberId);
	
	//check if delivery was previously 
	public CartItem findByMemberIdAndDeliveryId(int memberId, int delId);

}
