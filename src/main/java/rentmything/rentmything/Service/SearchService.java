package rentmything.rentmything.Service;


import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rentmything.rentmything.Dto.ReturningUserDto;
import rentmything.rentmything.Dto.SerchDto;
import rentmything.rentmything.Dto.UserDetailsDto;
import rentmything.rentmything.Model.User;
import rentmything.rentmything.Repository.UserRepository;
@Service
public class SearchService {
    @Autowired
    private UserRepository userRepository;
    
    public List<ReturningUserDto> location(SerchDto serchDto) {
        // Get latitude & longitude of current user
        UserDetailsDto userd = userRepository.findLatLngByEmail(serchDto.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Find owners within radius
        List<User> owners = userRepository.findOwnersWithItemsInRadiusEntities(
           userd.getLatitude(), userd.getLongitude(), serchDto.getRadius(), serchDto.getCategory()
        );

        // Map each User entity to ReturningUserDto
        return owners.stream()
            .map(u -> new ReturningUserDto(
                u.getName(),
                u.getPhoneNumber(),
                u.getLatitude(),
                u.getLongitude(),
                u.getItems()
            ))
            .collect(Collectors.toList());
    }
    
}
