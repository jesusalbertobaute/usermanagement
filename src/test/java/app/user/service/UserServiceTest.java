package app.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import app.jwt.JwtService;
import app.user.controller.RegisterRequest;
import app.user.controller.UserResponse;
import app.user.exception.EmailRegistradoException;
import app.user.model.Role;
import app.user.model.User;
import app.user.repository.PhoneRepository;
import app.user.repository.UserRepository;
import app.user.model.Phone;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	@Mock
    private PhoneRepository phoneRepository;
	@Mock
    private PasswordEncoder passwordEncoder;
	@Mock
	private JwtService jwtService;
	
	@InjectMocks
	private UserService userService;
	
	private User user;
	
	private User user2;
	
	@BeforeEach
	void setup() {
		user = User.builder()
				    .name("user")
			        .email("user@email.com")
			        .password("hmn!gLgfsu2")
			        .role(Role.ROLE_USER)
			        .lastLogin(LocalDateTime.now())
			        .creationDate(LocalDateTime.now())
			        .modifiedDate(LocalDateTime.now())
			        .isActive(true)
			        .build();
	
		userService.setFormatPassword("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@?&])[A-Za-z\\d!@?&]{8,15}$");
	}
	
	@Test
	@DisplayName("Test User Validation")
	public void buildMessageUserException() {
		
		assertTrue(userService.buildMessageUserException(user).isEmpty());
		
		user.setPassword("clave");
		assertFalse(userService.buildMessageUserException(user).isEmpty());
		
		user.setPassword("hmn!gLgfsu2");
		user.setEmail("jjfjf");
		assertFalse(userService.buildMessageUserException(user).isEmpty());
	}
	
	@Test
	@DisplayName("Test Register User")
	public void registerUser() {
		List<Phone> phones = new ArrayList<>();
		
		phones.add(Phone.builder().cityCode("11").number("7383842").countryCode("56").build());
		phones.add(Phone.builder().cityCode("11").number("1235182").countryCode("56").build());
		
		RegisterRequest registerRequest = RegisterRequest.builder()
				                                         .email("user2@email.com")
				                                         .name("user2")
				                                         .password("aunaisb!gLgfsu2")
				                                         .phones(phones)
				                                         .build();
		User userSaved = User.builder()
						    .name("user2")
					        .email("user2@email.com")
					        .role(Role.ROLE_USER)
					        .lastLogin(LocalDateTime.now())
					        .creationDate(LocalDateTime.now())
					        .modifiedDate(LocalDateTime.now())
					        .isActive(true)
					        .build();
		
		given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(false);
		
		when(userRepository.save(Mockito.any(User.class))).thenReturn(userSaved);
		
		var userResponse= new UserResponse("",userSaved);
		
		assertEquals(userService.createUser(registerRequest).getMessage(),userResponse.getMessage());
		
	}
	
	@Test
	@DisplayName("Test Register User - FailedEmail")
	public void registerUserFailed() {
		List<Phone> phones = new ArrayList<>();
		
		phones.add(Phone.builder().cityCode("11").number("7383842").countryCode("56").build());
		phones.add(Phone.builder().cityCode("11").number("1235182").countryCode("56").build());
		
		RegisterRequest registerRequest = RegisterRequest.builder()
				                                         .email("user2@email.com")
				                                         .name("user2")
				                                         .password("aunaisb!gLgfsu2")
				                                         .phones(phones)
				                                         .build();
		
		given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(true);
		
		assertThrows(EmailRegistradoException.class,() -> userService.createUser(registerRequest),"EmailRegistradoException no fue Lanzada");
		
	}

}
