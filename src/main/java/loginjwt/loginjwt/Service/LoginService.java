package loginjwt.loginjwt.Service;

import loginjwt.loginjwt.Dto.LoginResponse;
import loginjwt.loginjwt.Security.JwtCofing.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public <LoginRequest> LoginResponse login(LoginRequest request) {
        // Autentica usuário e senha (dispara UserDetailsService por baixo dos panos)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Se chegou aqui, autenticação deu certo
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gera o token JWT
        String token = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(token);
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}
