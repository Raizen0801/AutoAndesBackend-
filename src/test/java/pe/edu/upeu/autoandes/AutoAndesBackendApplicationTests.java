package pe.edu.upeu.autoandes;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AutoAndesBackendApplicationTests {

    @Test
    void testPasswordEncoding() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Auto2026*";
        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
    }
}
