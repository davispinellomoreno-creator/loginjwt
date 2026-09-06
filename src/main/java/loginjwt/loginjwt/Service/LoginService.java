package loginjwt.loginjwt.Service;

import loginjwt.loginjwt.Dto.LoginDto;
import loginjwt.loginjwt.Dto.LoginResponse;
import loginjwt.loginjwt.Repository.LoginRepository;
import loginjwt.loginjwt.Security.JwtCofing.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginRepository repository;
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

    public <RegisterRequest> void register(RegisterRequest request) {
        if (LoginRepository.save(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User() {
            @Override
            public boolean equals(Object another) {
                return false;
            }

            @Override
            public String toString() {
                return "";
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getFullName() {
                return "";
            }

            @Override
            public void setFullName(String s) {

            }

            @Override
            public Iterator<Group> getGroups() {
                return null;
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public void setPassword(String s) {

            }

            @Override
            public Iterator<Role> getRoles() {
                return null;
            }

            @Override
            public UserDatabase getUserDatabase() {
                return null;
            }

            @Override
            public String getUsername() {
                return "";
            }

            @Override
            public void setUsername(String s) {

            }

            @Override
            public void addGroup(Group group) {

            }

            @Override
            public void addRole(Role role) {

            }

            @Override
            public boolean isInGroup(Group group) {
                return false;
            }

            @Override
            public boolean isInRole(Role role) {
                return false;
            }

            @Override
            public void removeGroup(Group group) {

            }

            @Override
            public void removeGroups() {

            }

            @Override
            public void removeRole(Role role) {

            }

            @Override
            public void removeRoles() {

            }
        };
        user.getName(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.getRoles(Role.USER);

        LoginRepository.save(user);
    }
}
