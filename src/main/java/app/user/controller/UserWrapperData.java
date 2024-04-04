package app.user.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import app.user.model.Phone;
import app.user.model.Role;
import app.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserWrapperData {
	
	private UUID id;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private LocalDateTime creationDate;

	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime modifiedDate;

	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastLogin;

    private boolean isActive;

    private String token;
    
    public UserWrapperData(User user) {
    	if (user!=null) {
    		this.id = user.getId();
    		this.creationDate = user.getCreationDate();
    		this.modifiedDate = user.getModifiedDate();
    		this.lastLogin = user.getLastLogin();
    		this.isActive = user.isActive();
    		this.token = user.getToken();
    	}
    }
}
