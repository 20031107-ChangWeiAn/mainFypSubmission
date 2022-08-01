
package fyp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorldBayRepository extends JpaRepository<WorldBay, Integer> { // CRUD, Repository interface will extend the
																			// JpaRepository interface, The
																			// JpaRepository will provide methods such
																			// as findAll, findById, save, delete, etc
    @Query(value = "Select * from world_bay where deleted=0 ORDER BY Quantity,Price ASC",nativeQuery = true)
    List<WorldBay> getUndeletedItem();

    @Query(value = "Select * from world_bay where deleted=1", nativeQuery = true)
    List<WorldBay> getDeletedItem();
    
    public List<WorldBay> findTop2ByOrderByQuantityDesc();
}
