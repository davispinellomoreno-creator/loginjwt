package loginjwt.loginjwt.Service;

import loginjwt.loginjwt.Dto.LoginRequest;
import loginjwt.loginjwt.Dto.LoginResponse;
import loginjwt.loginjwt.Dto.RegisterRequest;
import loginjwt.loginjwt.Model.LoginEntity;
import loginjwt.loginjwt.Model.Role;
import loginjwt.loginjwt.Repository.LoginRepository;
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
    private final LoginRepository repository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(token);
    }

    public LoginResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        LoginEntity user = new LoginEntity();
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        repository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(token);
    }
}
