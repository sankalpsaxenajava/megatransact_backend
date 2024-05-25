package com.megatransact.dto;

import lombok.Data;

/**
 * DTO for updating user information.
 * Currently only phone number is allowed to be updated.
 * @author romulo.domingos
 * @since 1.0
 */
@Data
public class UserUpdateDTO {
    private String email;
    private String phoneNumber;
}
