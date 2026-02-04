package rentmything.rentmything.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rentmything.rentmything.Dto.UserDetailsDto;
import rentmything.rentmything.Model.Category;
import rentmything.rentmything.Model.User;
@Repository
public interface UserRepository extends JpaRepository<User ,Long>{
    boolean existsByEmail(String email);
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT new rentmything.rentmything.Dto.UserDetailsDto(u.latitude, u.longitude) FROM User u WHERE u.email = :email")
Optional<UserDetailsDto> findLatLngByEmail(@Param("email") String email);

@Query(""" 
    SELECT DISTINCT u
    FROM User u
    LEFT JOIN FETCH u.items i
    WHERE u.role = 'OWNER'
      AND i.category = :category
      AND u.latitude IS NOT NULL
      AND u.longitude IS NOT NULL
      AND u.latitude <> 0.0
      AND u.longitude <> 0.0
      AND (6371 * acos(
            cos(radians(:latitude)) * cos(radians(u.latitude)) *
            cos(radians(u.longitude) - radians(:longitude)) +
            sin(radians(:latitude)) * sin(radians(u.latitude))
      )) < :radius
""")
List<User> findOwnersWithItemsInRadiusEntities(
    @Param("latitude") double latitude,
    @Param("longitude") double longitude,
    @Param("radius") double radius,
    @Param("category") Category category
);




}
