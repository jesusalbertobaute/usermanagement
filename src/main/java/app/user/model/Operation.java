package app.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Operation {
	USER_READ("USER_READ"),
    USER_UPDATE("USER_UPDATE"),
    USER_CREATE("USER_CREATE"),
    USER_DELETE("USER_DELETE"),
    USER_VERIFY("USER_VERIFY");

    @Getter
    private final String operation;
}
