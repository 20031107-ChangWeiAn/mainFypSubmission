
package fyp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Integer> {

	// SELECT * FROM CartItem where member_id = id;
	// get the list of cartitems based on member id
	public List<WishList> findByMemberId(int id);

	// check if item was previously added by user
	public WishList findByMemberIdAndItemId(int memberId, int itemId);
}
