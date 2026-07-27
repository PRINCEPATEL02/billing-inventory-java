package com.billing.dto;

import com.billing.enums.Role;

public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String fullName;
    private Role role;

    public LoginResponse() {}

    public LoginResponse(String token, String type, Long id, String username, String fullName, Role role) {
        this.token = token;
        if (type != null) this.type = type;
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String fullName;
        private Role role;

        LoginResponseBuilder() {}

        public LoginResponseBuilder token(String token) { this.token = token; return this; }
        public LoginResponseBuilder type(String type) { this.type = type; return this; }
        public LoginResponseBuilder id(Long id) { this.id = id; return this; }
        public LoginResponseBuilder username(String username) { this.username = username; return this; }
        public LoginResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public LoginResponseBuilder role(Role role) { this.role = role; return this; }

        public LoginResponse build() {
            return new LoginResponse(token, type, id, username, fullName, role);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}