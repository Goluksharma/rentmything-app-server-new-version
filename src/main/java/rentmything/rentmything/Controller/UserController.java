package rentmything.rentmything.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import rentmything.rentmything.Dto.ApiResponse;
import rentmything.rentmything.Dto.DeleteAccDto;
import rentmything.rentmything.Dto.DeleteDto;
import rentmything.rentmything.Dto.FetchDto;
import rentmything.rentmything.Dto.ItemDto;
import rentmything.rentmything.Dto.LocationRequest;
import rentmything.rentmything.Dto.OwnerProfileDto;
import rentmything.rentmything.Dto.RegistrationDto;
import rentmything.rentmything.Dto.ReturningUserDto;
import rentmything.rentmything.Dto.SerchDto;
import rentmything.rentmything.Mapper.OwnerMapper;
import rentmything.rentmything.Model.Category;
import rentmything.rentmything.Model.Item;
import rentmything.rentmything.Model.Role;
import rentmything.rentmything.Model.User;
import rentmything.rentmything.Repository.ItemRepository;
import rentmything.rentmything.Repository.UserRepository;
import rentmything.rentmything.Service.ItemService;
import rentmything.rentmything.Service.JWTService;
import rentmything.rentmything.Service.OtpService;
import rentmything.rentmything.Service.SearchService;
import rentmything.rentmything.Service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;

    // Step 1: Send OTP to the email
    @PostMapping("/sendotp")
    public ResponseEntity<ApiResponse> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "EMAIL_TAKEN", "Email already registered"));
        }

        String otp = otpService.generateOtp();
        boolean sent = otpService.sendOtp(email, otp);
        otpService.saveOtpToRedis(email, otp, 3);
        

        if (!sent) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "INVALID_EMAIL", "Email address does not exist or could not receive mail"));
        }
        return ResponseEntity.ok(new ApiResponse(true, "OTP_SENT", "OTP sent successfully"));
    }

    // Step 2: Verify OTP entered by user
    @PostMapping("/verifyotp")
    public ResponseEntity<ApiResponse> submitOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String submittedOtp = body.get("otp");
        
       
if (email == null || submittedOtp == null) {
    return ResponseEntity.badRequest().body(new ApiResponse(false, "OTP_REQUIRED", "Email and OTP are required"));
}

boolean verified = otpService.verifyOtpFromRedis(email, submittedOtp);
if (!verified) {
    return ResponseEntity.status(400).body(new ApiResponse(false, "OTP_INVALID_OR_EXPIRED", "OTP is invalid or expired"));
}
otpService.markEmailVerified(email, 5); 
return ResponseEntity.ok(new ApiResponse(true, "OTP_VERIFIED", "OTP verified successfully"));

    }
 
   
    
    
    // Step 3: Register user (only if OTP verified and not expired)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> RegisterAsOwner(@Valid @RequestBody RegistrationDto Dto) {
    

       if (!otpService.isEmailVerified(Dto.getEmail())) {
        return ResponseEntity.status(400)
                .body(new ApiResponse(false, "OTP_NOT_VERIFIED", "OTP not verified or expired"));
    }

    boolean success = userService.registerUser(Dto);
    if (success) {
        // ✅ Optional: remove the verification flag after registration
        otpService.deleteEmailVerifiedFlag(Dto.getEmail());

        return ResponseEntity.ok(new ApiResponse(true, "REGISTRATION_SUCCESS", "Registration successful"));
    } else {
        return ResponseEntity.status(400)
                .body(new ApiResponse(false, "EMAIL_TAKEN", "Email already registered"));
    }
    }
   
    

    // Step 4: Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password"); 

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "MISSING_CREDENTIALS", "Email and Password are needed"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            

            String token=jwtService.generateToken(email);

            Map<String, Object> userdetail = Map.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "role", user.getRole(),
                "token",token
                
            );

            return ResponseEntity.ok(new ApiResponse(true, "LOGIN_SUCCESS", "Login Successful",userdetail));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "AUTH_FAILED", "Invalid email or password",null));
        }
    }
    @PostMapping("/save-location")
    public ResponseEntity<ApiResponse> saveLocation(@Valid @RequestBody LocationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());
        userRepository.save(user);
    
       
    
        ApiResponse response = new ApiResponse(
            true,
            "200",
            "Location updated successfully",
            null
        );
    
        return ResponseEntity.ok(response); 
    }
    @GetMapping("/categories")
public ResponseEntity<List<String>> getCategories() {
    List<String> categories = java.util.stream.Stream.of(Category.values())
                                    .map(Enum::name)
                                    .collect(java.util.stream.Collectors.toList());
    return ResponseEntity.ok(categories);
}
@Autowired
private ItemService itemService;

@PostMapping("/add-service")
public ResponseEntity<ApiResponse> addItem(@RequestBody ItemDto dto) {
    try {
        Item item = itemService.addItem(dto);
        return ResponseEntity.ok(new ApiResponse(true, "ITEM_ADDED", "Item added successfully", item));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "ITEM_ADD_FAILED", e.getMessage(), null));
    }

}
@Autowired
private OwnerMapper ownerMapper;
@PostMapping("/fetching") 
public ResponseEntity<ApiResponse> fetching(@RequestBody FetchDto dto) {
    User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (dto.getRole() != Role.OWNER) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ApiResponse(false, "ACCESS_DENIED", "Access denied"));
    }

    OwnerProfileDto profile = ownerMapper.toDto(user);
    return ResponseEntity.ok(new ApiResponse(true, "SUCCESS", "Success", profile));
}
@Autowired
private ItemRepository itemRepository;
@DeleteMapping("/deleteItem")
public ResponseEntity<ApiResponse> deleteItem(@RequestBody DeleteDto dto) {
    int rowsAffected = itemRepository.deleteByTitleAndOwnerEmail(dto.getTitle(), dto.getEmail());

    if (rowsAffected > 0) {
        return ResponseEntity.ok(new ApiResponse(true, "SUCCESS", "Item deleted"));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(false, "FAILURE", "Item not found or not owned by this user"));
    }
}
@Autowired
private SearchService searchService;
@PostMapping("/serch")
public ResponseEntity<ApiResponse> serch(@RequestBody SerchDto serchDto) {
    List<ReturningUserDto> userdeatials = searchService.location(serchDto);
    ApiResponse response = new ApiResponse(true, "SEARCH_SUCCESS", "Search completed successfully", userdeatials);
    return ResponseEntity.ok(response);
}
@DeleteMapping("/delete-account")
public ResponseEntity<ApiResponse> DeleteAccount(@RequestBody DeleteAccDto dto) {
    
    User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    userRepository.delete(user);
    return ResponseEntity.ok(new ApiResponse(true, "ACCOUNT_DELETED", "Account deleted successfully"));
}
}