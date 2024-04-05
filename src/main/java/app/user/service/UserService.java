package app.user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import app.jwt.JwtService;
import app.user.controller.RegisterRequest;
import app.user.controller.UserResponse;
import app.user.exception.EmailRegistradoException;
import app.user.exception.UserException;
import app.user.model.Phone;
import app.user.model.Role;
import app.user.model.User;
import app.user.repository.PhoneRepository;
import app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @Value("${application.security.password.format}")
    private String formatPassword;
    
    private final String formatEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    @Transactional
    public UserResponse createUser(RegisterRequest request) throws UserException,EmailRegistradoException {
	    User user = User.builder()
	        .name(request.getName())
	        .email(request.getEmail())
	        .password(request.getPassword())
	        .role(Role.ROLE_USER)
	        .lastLogin(LocalDateTime.now())
	        .creationDate(LocalDateTime.now())
	        .modifiedDate(LocalDateTime.now())
	        .isActive(true)
	        .build();
	    
	    String messageException = buildMessageUserException(user);
	    if (!ObjectUtils.isEmpty(messageException)) throw new UserException(messageException); 
	    
	    if (userRepository.existsByEmail(user.getEmail())) throw new EmailRegistradoException("El correo ya esta registrado");
	    
	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    String token = jwtService.generateToken(user);
	    user.setToken(token);
	    
	    var userSaved= userRepository.save(user);

	    if (!ObjectUtils.isEmpty(request.getPhones())) {
	        List<Phone> phones = request.getPhones().stream().map(phone -> {
	        	phone.setUser(userSaved);
	        	return phone;
	        }).toList();
	        phoneRepository.saveAll(phones);
	    }
	    
	    var userResponse= new UserResponse("",user);
        return userResponse;
   }
    
    
   public String buildMessageUserException(User user) {
	   
	   StringBuilder message= new StringBuilder("");
	   
	   if (user==null) { 
		   message.append("Usuario es Null\n");
		   return message.toString();
	   }
	   
	   if (ObjectUtils.isEmpty(user.getEmail())) { 
		   message.append("El Email no puede estar vacio\n");
		   return message.toString();
	   }
	   

	   if (!user.getEmail().matches(formatEmail)) {
		   message.append("El Email no tiene un formato valido");
		   return message.toString();
	   }
	   
	   if (ObjectUtils.isEmpty(user.getPassword())) {
		   message.append("El Password no puede estar vacio");
		   return message.toString();
	   }
	  
	   if (!user.getPassword().matches(formatPassword)) {
		   message.append("El Password no tiene un formato valido");
		   return message.toString();
	   }

	   
	   return message.toString();
   }
   
   public void setFormatPassword(String formatPassword) {
	   this.formatPassword = formatPassword;
   }
  
}
