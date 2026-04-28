package com.makernav.categorize.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        @NotBlank String token
) {}
