package virtualpets.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import virtualpets.models.Pet;
import virtualpets.models.User;
import virtualpets.services.PetService;
import virtualpets.services.UserService;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    private final PetService petService;
    private final UserService userService;

    public AuthorizationFilter(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        logger.debug("Authorization check for {} {}", method, requestURI);

        // Skip authorization for public endpoints
        if (isPublicEndpoint(requestURI)) {
            logger.debug("Public endpoint, skipping authorization");
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized access attempt to {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
            return;
        }

        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            logger.warn("User not found: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"User not found\"}");
            return;
        }

        User currentUser = userOpt.get();

        // Check authorization based on endpoint and user role
        if (!isAuthorized(requestURI, method, currentUser)) {
            logger.warn("Access denied for user {} to {} {}", username, method, requestURI);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"Access denied\"}");
            return;
        }

        logger.debug("Authorization granted for user {} to {} {}", username, method, requestURI);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.startsWith("/api/auth/") ||
                requestURI.startsWith("/h2-console/") ||
                requestURI.startsWith("/actuator/") ||
                requestURI.equals("/") ||
                requestURI.startsWith("/static/");
    }

    private boolean isAuthorized(String requestURI, String method, User user) {
        // Admin has access to everything
        if ("ROLE_ADMIN".equals(user.getRole().name())) {
            logger.debug("Admin access granted");
            return true;
        }

        // Admin endpoints - only for admins
        if (requestURI.startsWith("/api/admin/")) {
            logger.debug("Admin endpoint access denied for regular user");
            return false;
        }

        // Pet endpoints authorization
        if (requestURI.startsWith("/api/pets")) {
            return isPetEndpointAuthorized(requestURI, method, user);
        }

        // Default: allow access to other endpoints for authenticated users
        return true;
    }

    private boolean isPetEndpointAuthorized(String requestURI, String method, User user) {
        // Extract pet ID from URL if present
        String[] pathParts = requestURI.split("/");

        // /api/pets - GET (list user's pets) - allowed for users
        if (pathParts.length == 3 && "GET".equals(method)) {
            return true;
        }

        // /api/pets - POST (create pet) - allowed for users
        if (pathParts.length == 3 && "POST".equals(method)) {
            return true;
        }

        // /api/pets/{id} - operations on specific pet
        if (pathParts.length >= 4) {
            try {
                Long petId = Long.parseLong(pathParts[3]);
                return isPetOwnerOrAdmin(petId, user);
            } catch (NumberFormatException e) {
                logger.warn("Invalid pet ID in URL: {}", pathParts[3]);
                return false;
            }
        }

        return false;
    }

    private boolean isPetOwnerOrAdmin(Long petId, User user) {
        try {
            Optional<Pet> petOpt = petService.findById(petId);

            if (petOpt.isEmpty()) {
                logger.debug("Pet not found: {}", petId);
                return false; // Pet doesn't exist
            }

            Pet pet = petOpt.get();
            boolean isOwner = pet.getOwner().getId().equals(user.getId());
            boolean isAdmin = "ROLE_ADMIN".equals(user.getRole().name());

            logger.debug("Pet {} ownership check: user={}, owner={}, isOwner={}, isAdmin={}",
                    petId, user.getId(), pet.getOwner().getId(), isOwner, isAdmin);

            return isOwner || isAdmin;

        } catch (Exception e) {
            logger.error("Error checking pet ownership for pet {} and user {}: {}",
                    petId, user.getId(), e.getMessage());
            return false;
        }
    }
}