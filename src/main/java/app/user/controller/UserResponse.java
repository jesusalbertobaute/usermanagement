package app.user.controller;

import app.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {
	private String message;
    private UserWrapperData user;
    
    public UserResponse(String message,User user) {
    	this.message = message;
    	this.user = new UserWrapperData(user);
    }
    
    
}
