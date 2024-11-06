package com.api.dto.Base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class BaseDto {

    private String createdBy;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime createdDateTime = LocalDateTime.now();

    private String lastModifiedBy;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    public String currentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        return userDetails.getUsername();
    }
}
