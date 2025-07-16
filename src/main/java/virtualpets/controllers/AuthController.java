package virtualpets.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import virtualpets.dtos.UserCreateDTO;
import virtualpets.dtos.UserDTO;
import virtualpets.exceptions.BadRequestException;
import virtualpets.models.User;
import virtualpets.security.JwtTokenProvider;
import virtualpets.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = new User();
            user.setUsername(userCreateDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

            User savedUser = userService.createUser(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(savedUser.getId());
            userDTO.setUsername(savedUser.getUsername());
            userDTO.setRole(savedUser.getRole().name());

            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserCreateDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);
            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setRole(user.getRole().name());


            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userDTO);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Invalid credentials");
        }
    }
}