
package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.username = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

}
