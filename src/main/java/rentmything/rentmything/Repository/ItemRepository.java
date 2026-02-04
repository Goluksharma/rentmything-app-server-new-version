package rentmything.rentmything.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import rentmything.rentmything.Model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @Modifying
    @Transactional
    @Query("DELETE FROM Item i WHERE i.title = :title AND i.owner.email = :email")
    int deleteByTitleAndOwnerEmail(@Param("title") String title, @Param("email") String email);
    


    
}
