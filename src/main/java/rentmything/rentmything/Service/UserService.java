package rentmything.rentmything.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rentmything.rentmything.Dto.RegistrationDto;
import rentmything.rentmything.Mapper.UserMapper;
import rentmything.rentmything.Model.User;
import rentmything.rentmything.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public boolean registerUser(RegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return false; // ✅ Prevent duplicate registration
        }
    
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return true;
    }
    
}