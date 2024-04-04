package app.user.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.user.exception.EmailRegistradoException;
import app.user.exception.UserException;
import app.user.service.LoginService;
import app.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserController {
	private final UserService userService;  
	private final LoginService loginService;

	@PostMapping(path="/register",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER_CREATE')")
	public ResponseEntity<UserResponse> register(
			@RequestBody RegisterRequest  request
			) {
		UserResponse userResponse= null;
		try {
			userResponse = userService.createUser(request);
			userResponse.setMessage("Usuario Agregado con Exito");
			return new ResponseEntity<>(userResponse,HttpStatus.CREATED);
		}catch(UserException | EmailRegistradoException e) {
			userResponse = new UserResponse(e.getMessage(),new UserWrapperData());
			return new ResponseEntity<>(userResponse,HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping(path="/login",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> login(
			@RequestBody LoginRequest  request
			) {
		
		LoginResponse loginResponse= null;
		
		try {
			loginResponse = loginService.authenticate(request);
			loginResponse.setMessage("Usuario Autenticado con Exito");
			return new ResponseEntity<>(loginResponse,HttpStatus.OK);
		}catch(NoSuchElementException | AuthenticationException e) {
			loginResponse = new LoginResponse("Usuario no pudo ser autenticado","");
			return new ResponseEntity<>(loginResponse,HttpStatus.UNAUTHORIZED);
		}catch(Exception e) {
			loginResponse = new LoginResponse("Ha ocurrido un error al intentar auntenticar al usuario","");
			return new ResponseEntity<>(loginResponse,HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping(path="/verify",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USER_VERIFY')")
	public ResponseEntity<MessageResponse> verifyUser() {
		MessageResponse messageResponse = new MessageResponse("El Usuario tiene este privilegio");
		return new ResponseEntity<>(messageResponse,HttpStatus.OK);
		
		

	}

}
