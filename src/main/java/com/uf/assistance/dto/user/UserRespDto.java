package com.uf.assistance.dto.user;

import com.uf.assistance.domain.user.User;
import com.uf.assistance.domain.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRespDto {
    private Long id;
    private String userId;
    private String username;
    private String email;
    private UserRole role;
    private boolean social;

    public static UserRespDto from(User user) {
        return new UserRespDto(user.getId(), user.getUserId(), user.getUsername(), user.getEmail(), user.getRole(), user.isSocial());
    }
}
