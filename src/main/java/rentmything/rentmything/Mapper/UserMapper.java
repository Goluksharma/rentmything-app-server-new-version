package rentmything.rentmything.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import rentmything.rentmything.Dto.RegistrationDto;
import rentmything.rentmything.Model.User;
import rentmything.rentmything.Model.Role;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    @Mapping(target = "latitude", constant = "0.0")
    @Mapping(target = "longitude", constant = "0.0")
    @Mapping(target = "items", ignore = true)
    User toEntity(RegistrationDto dto);

    @Named("stringToRole")
    default Role mapRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}