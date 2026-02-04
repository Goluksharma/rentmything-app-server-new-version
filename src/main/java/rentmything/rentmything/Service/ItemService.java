package rentmything.rentmything.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rentmything.rentmything.Dto.ItemDto;
import rentmything.rentmything.Model.Item;
import rentmything.rentmything.Model.User;
import rentmything.rentmything.Repository.ItemRepository;
import rentmything.rentmything.Repository.UserRepository;

@Service
public class ItemService {

    @Autowired 
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public Item addItem(ItemDto dto) throws Exception {
        if (!"OWNER".equalsIgnoreCase(dto.getRole())) {
            throw new Exception("Only OWNERs can add products");
        }

        User owner = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Item item = new Item();
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setAddress(dto.getAddress());
        item.setCategory(dto.getCategory());
        item.setOwner(owner);

        return itemRepository.save(item);
    }
}
