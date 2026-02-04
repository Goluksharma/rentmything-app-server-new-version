package rentmything.rentmything.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rentmything.rentmything.Dto.ItemDto;
import rentmything.rentmything.Dto.OwnerProfileDto;
import rentmything.rentmything.Model.Item;
import rentmything.rentmything.Model.User;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerProfileDto toDto(User user);
    @Mapping(target = "email",ignore = true)
    @Mapping(target = "role", ignore = true)
    ItemDto toItemDto(Item item);

}
