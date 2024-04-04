package app.user.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import app.jwt.JwtService;
import app.user.controller.LoginRequest;
import app.user.controller.LoginResponse;
import app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final UserRepository repository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public LoginResponse authenticate(LoginRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
						)
				);
		var user = repository.findByEmail(request.getEmail())
				.orElseThrow();


		String jwtToken = user.getToken();
		if (ObjectUtils.isEmpty(jwtToken) || !jwtService.isTokenValid(jwtToken, user)) {
			jwtToken = jwtService.generateToken(user);
		}

		user.setLastLogin(LocalDateTime.now());
		repository.save(user);

		return LoginResponse.builder()
				.token(jwtToken)
				.build();
	}
}
