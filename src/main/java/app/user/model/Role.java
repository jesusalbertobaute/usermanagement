package app.user.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	ROLE_USER(
            Set.of(Operation.USER_SELF_READ)
    ),ROLE_ADMIN(
            Set.of(
            	   Operation.USER_SELF_READ,	
                   Operation.USER_CREATE,
                   Operation.USER_UPDATE,
                   Operation.USER_READ,
                   Operation.USER_DELETE
                   )
   );

    @Getter
    private final Set<Operation> permisions;
    

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermisions()
                          .stream()
                          .map(operation -> new SimpleGrantedAuthority(operation.getOperation()))
                          .collect(Collectors.toList());

        return authorities;
    }
}

