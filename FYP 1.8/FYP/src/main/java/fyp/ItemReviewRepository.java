package fyp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemReviewRepository extends JpaRepository<ItemReview, Integer> {

    @Query(value = "Select * from item_review where itemid=?1",nativeQuery = true)
    List<ItemReview> findByItemId(String id);

}