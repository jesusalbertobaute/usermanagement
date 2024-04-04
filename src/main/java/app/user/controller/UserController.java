package app.user.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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
public class UserController {
	private final UserService userService;  
	private final LoginService loginService;

	@PostMapping(path="/register",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
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

}
