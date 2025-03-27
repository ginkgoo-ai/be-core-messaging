package com.ginkgooai.dto;

import com.ginkgooai.domain.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateDto {
    
    private Long id;
    
    @NotBlank(message = "Template name cannot be empty")
    private String name;
    
    private String description;
    
    @NotNull(message = "Email type cannot be null")
    private EmailType emailType;
    
    @NotBlank(message = "Email subject cannot be empty")
    private String subject;
    
    private String content;
    
    private String properties;
} 