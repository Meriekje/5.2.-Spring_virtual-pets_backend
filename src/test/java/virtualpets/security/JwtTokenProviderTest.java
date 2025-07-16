package virtualpets.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Use a test secret key with sufficient length
        String testSecret = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
        int testExpiration = 86400000; // 24 hours

        jwtTokenProvider = new JwtTokenProvider(testSecret, testExpiration);

        // Create test authentication
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void generateToken_Success() {
        // When
        String token = jwtTokenProvider.generateToken(authentication);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void getUsernameFromToken_Success() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        String username = jwtTokenProvider.getUsernameFromToken(token);

        // Then
        assertEquals("testuser", username);
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_EmptyToken_ReturnsFalse() {
        // Given
        String emptyToken = "";

        // When
        boolean isValid = jwtTokenProvider.validateToken(emptyToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_NullToken_ReturnsFalse() {
        // Given
        String nullToken = null;

        // When
        boolean isValid = jwtTokenProvider.validateToken(nullToken);

        // Then
        assertFalse(isValid);
    }
}