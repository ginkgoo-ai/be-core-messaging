package com.ginkgooai.controller;

import com.ginkgooai.domain.EmailType;
import com.ginkgooai.dto.EmailTemplateDto;
import com.ginkgooai.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Email Template Controller
 */
@RestController
@RequestMapping("/api/email-templates")
@RequiredArgsConstructor
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    /**
     * Create a new email template
     *
     * @param emailTemplateDto email template DTO
     * @return created email template
     */
    @PostMapping
    public ResponseEntity<EmailTemplateDto> createEmailTemplate(@Valid @RequestBody EmailTemplateDto emailTemplateDto) {
        EmailTemplateDto createdTemplate = emailTemplateService.createEmailTemplate(emailTemplateDto);
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    /**
     * Get email template by ID
     *
     * @param id email template ID
     * @return email template
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailTemplateDto> getEmailTemplateById(@PathVariable Long id) {
        EmailTemplateDto emailTemplateDto = emailTemplateService.getEmailTemplateById(id);
        return ResponseEntity.ok(emailTemplateDto);
    }

    /**
     * Get all email templates with optional filtering
     *
     * @param name optional name filter
     * @param type optional type filter
     * @return list of email templates
     */
    @GetMapping
    public ResponseEntity<List<EmailTemplateDto>> getAllEmailTemplates(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) EmailType type) {
        List<EmailTemplateDto> templates = emailTemplateService.getAllEmailTemplates(name, type);
        return ResponseEntity.ok(templates);
    }

    /**
     * Update an email template
     *
     * @param id email template ID
     * @param emailTemplateDto email template DTO
     * @return updated email template
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailTemplateDto> updateEmailTemplate(
            @PathVariable Long id, 
            @Valid @RequestBody EmailTemplateDto emailTemplateDto) {
        EmailTemplateDto updatedTemplate = emailTemplateService.updateEmailTemplate(id, emailTemplateDto);
        return ResponseEntity.ok(updatedTemplate);
    }

    /**
     * Delete an email template
     *
     * @param id email template ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailTemplate(@PathVariable Long id) {
        emailTemplateService.deleteEmailTemplate(id);
        return ResponseEntity.noContent().build();
    }
} 