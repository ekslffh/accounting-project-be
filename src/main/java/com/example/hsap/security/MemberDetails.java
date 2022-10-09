package com.example.hsap.security;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDetails {
    String userId;
    Collection<? extends GrantedAuthority> authorities;
}
