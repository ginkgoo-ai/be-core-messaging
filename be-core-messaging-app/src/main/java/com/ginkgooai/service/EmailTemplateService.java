package com.ginkgooai.service;

import com.ginkgooai.domain.EmailType;
import com.ginkgooai.dto.EmailTemplateDto;

import java.util.List;

/**
 * Email Template Service Interface
 */
public interface EmailTemplateService {
    
    /**
     * Create a new email template
     *
     * @param emailTemplateDto email template DTO
     * @return created email template DTO
     */
    EmailTemplateDto createEmailTemplate(EmailTemplateDto emailTemplateDto);
    
    /**
     * Get email template by ID
     *
     * @param id email template ID
     * @return email template DTO
     */
    EmailTemplateDto getEmailTemplateById(Long id);
    
    /**
     * Get all email templates with optional filtering
     *
     * @param name email template name (optional)
     * @param emailType email template type (optional)
     * @return list of email template DTOs
     */
    List<EmailTemplateDto> getAllEmailTemplates(String name, EmailType emailType);
    
    /**
     * Update an email template
     *
     * @param id email template ID
     * @param emailTemplateDto email template DTO
     * @return updated email template DTO
     */
    EmailTemplateDto updateEmailTemplate(Long id, EmailTemplateDto emailTemplateDto);
    
    /**
     * Delete an email template
     *
     * @param id email template ID
     */
    void deleteEmailTemplate(Long id);
} 