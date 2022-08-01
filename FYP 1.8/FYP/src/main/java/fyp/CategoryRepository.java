
package fyp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> { // CRUD, Repository interface will extend
																				// the JpaRepository interface, The
																				// JpaRepository will provide methods
																				// such as findAll, findById, save,
																				// delete, etc

}
