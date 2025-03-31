package com.ginkgooai.dto;

import com.ginkgooai.domain.EmailTemplate;
import com.ginkgooai.domain.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email template data transfer object")
public class EmailTemplateDto {
    
    @Schema(description = "Unique identifier of the email template")
    private String id;
    
    @Schema(description = "Name of the email template", example = "Welcome Email")
    @NotBlank(message = "Template name cannot be empty")
    private String name;
    
    @Schema(description = "Description of the email template", example = "Template for sending welcome emails to new users")
    private String description;
    
    @Schema(description = "Type of the email template", example = "INVITATION | INVITE_UPLOAD")
    @NotNull(message = "Email type cannot be null")
    private EmailType emailType;
    
    @Schema(description = "Subject line of the email", example = "Welcome to Our Platform!")
    @NotBlank(message = "Email subject cannot be empty")
    private String subject;
    
    @Schema(description = "HTML content of the email template", example = "<html><body><h1>Welcome!</h1><p>Thank you for joining our platform.</p></body></html>")
    private String content;
    
    @Schema(description = "Email Place holders", example = "[\"FIRST_NAME\",\"LAST_NAME\"]")
    private List<String> placeholders;


    /**
     * 将EmailTemplate实体转换为DTO
     */
    public static EmailTemplateDto convertToDto(EmailTemplate emailTemplate) {
        return EmailTemplateDto.builder()
                .id(emailTemplate.getId())
                .name(emailTemplate.getName())
                .description(emailTemplate.getDescription())
                .emailType(emailTemplate.getEmailType())
                .subject(emailTemplate.getSubject())
                .content(emailTemplate.getContent())
                .placeholders(Arrays.asList(emailTemplate.getPlaceholders().split(",")))
                .build();
    }

    public static void coverEmailTemplate(EmailTemplateDto emailTemplateDto, EmailTemplate existingTemplate) {
        existingTemplate.setName(emailTemplateDto.getName());
        existingTemplate.setDescription(emailTemplateDto.getDescription());
        existingTemplate.setEmailType(emailTemplateDto.getEmailType());
        existingTemplate.setSubject(emailTemplateDto.getSubject());
        existingTemplate.setContent(emailTemplateDto.getContent());
        existingTemplate.setPlaceholders(String.join(",", emailTemplateDto.getPlaceholders()));
    }

    /**
     * 将DTO转换为EmailTemplate实体
     */
    public static EmailTemplate convertToEntity(EmailTemplateDto emailTemplateDto) {
        EmailTemplate emailTemplate = new EmailTemplate();
        coverEmailTemplate(emailTemplateDto, emailTemplate);
        return emailTemplate;
    }
} 