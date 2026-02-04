package rentmything.rentmything.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    @Autowired
    private JavaMailSender mailSender;

    // ------------------- REDIS -------------------
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveOtpToRedis(String email, String otp, long minutes) {
        redisTemplate.opsForValue().set(email, otp, minutes, TimeUnit.MINUTES);
    }

    public boolean verifyOtpFromRedis(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(email); // delete after verification
            return true;
        }
        return false;
    }
    // Mark email as OTP-verified
public void markEmailVerified(String email, long minutes) {
    redisTemplate.opsForValue().set(email + ":verified", "true", minutes, TimeUnit.MINUTES);
}

// Check if email is OTP-verified
public boolean isEmailVerified(String email) {
    String verified = redisTemplate.opsForValue().get(email + ":verified");
    return "true".equals(verified);
}


    // ------------------- YOUR EXISTING CODE -------------------
    // Generate OTP
    public String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); 
        }
        return otp.toString();
    }

    // Send OTP via real email
    public boolean sendOtp(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP Code for RentMyThing");
            message.setText("Your OTP code is: " + otp);
            message.setFrom("rentmythingapp@gmail.com");
    
            mailSender.send(message); // Will throw if address is invalid/unresolvable
            return true;
    
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            return false;
        }
    }

    public void deleteEmailVerifiedFlag(String email) {
    redisTemplate.delete(email + ":verified");
}

}
