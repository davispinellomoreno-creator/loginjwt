package loginjwt.loginjwt.Service;

import loginjwt.loginjwt.Dto.ForgotPasswordRequest;
import loginjwt.loginjwt.Dto.ForgotPasswordResponse;
import loginjwt.loginjwt.Dto.LoginRequest;
import loginjwt.loginjwt.Dto.LoginResponse;
import loginjwt.loginjwt.Dto.RegisterRequest;
import loginjwt.loginjwt.Dto.ResetPasswordRequest;
import loginjwt.loginjwt.Exception.EmailAlreadyExistsException;
import loginjwt.loginjwt.Exception.InvalidCredentialsException;
import loginjwt.loginjwt.Exception.InvalidTokenException;
import loginjwt.loginjwt.Exception.TokenExpiredException;
import loginjwt.loginjwt.Exception.UserNotFoundException;
import loginjwt.loginjwt.Model.LoginEntity;
import loginjwt.loginjwt.Model.PasswordResetToken;
import loginjwt.loginjwt.Model.Role;
import loginjwt.loginjwt.Repository.LoginRepository;
import loginjwt.loginjwt.Repository.PasswordResetTokenRepository;
import loginjwt.loginjwt.Security.JwtCofing.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginRepository repository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(token);
    }

    public LoginResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
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

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {

        if (!repository.existsByEmail(request.email())) {
            throw new UserNotFoundException(request.email());
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken(token, request.email(), expiry);
        resetTokenRepository.save(resetToken);

        return new ForgotPasswordResponse(
                "Token de recuperação gerado com sucesso",
                token
        );
    }

    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = resetTokenRepository.findByToken(request.token())
                .orElseThrow(InvalidTokenException::new);

        if (resetToken.isUsed()) {
            throw new InvalidTokenException();
        }

        if (resetToken.isExpired()) {
            throw new TokenExpiredException();
        }

        LoginEntity user = repository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new UserNotFoundException(resetToken.getEmail()));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }
}