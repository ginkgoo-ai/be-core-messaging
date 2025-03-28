package com.ginkgooai.dto;

import com.ginkgooai.domain.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email template data transfer object")
public class EmailTemplateDto {
    
    @Schema(description = "Unique identifier of the email template")
    private Long id;
    
    @Schema(description = "Name of the email template", example = "Welcome Email")
    @NotBlank(message = "Template name cannot be empty")
    private String name;
    
    @Schema(description = "Description of the email template", example = "Template for sending welcome emails to new users")
    private String description;
    
    @Schema(description = "Type of the email template", example = "WELCOME")
    @NotNull(message = "Email type cannot be null")
    private EmailType emailType;
    
    @Schema(description = "Subject line of the email", example = "Welcome to Our Platform!")
    @NotBlank(message = "Email subject cannot be empty")
    private String subject;
    
    @Schema(description = "HTML content of the email template", example = "<html><body><h1>Welcome!</h1><p>Thank you for joining our platform.</p></body></html>")
    private String content;
    
    @Schema(description = "Additional properties in JSON format", example = "{\"sender\":\"noreply@example.com\",\"replyTo\":\"support@example.com\"}")
    private String properties;
} 