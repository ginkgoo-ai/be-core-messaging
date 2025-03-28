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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Email Template Controller
 */
@RestController
@RequestMapping("/api/email-templates")
@RequiredArgsConstructor
@Tag(name = "Email Template", description = "Email template management APIs")
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    /**
     * Create a new email template
     *
     * @param emailTemplateDto email template DTO
     * @return created email template
     */
    @Operation(summary = "Create a new email template", description = "Creates a new email template with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Email template created successfully",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<EmailTemplateDto> createEmailTemplate(
            @Parameter(description = "Email template details", required = true)
            @Valid @RequestBody EmailTemplateDto emailTemplateDto) {
        EmailTemplateDto createdTemplate = emailTemplateService.createEmailTemplate(emailTemplateDto);
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    /**
     * Get email template by ID
     *
     * @param id email template ID
     * @return email template
     */
    @Operation(summary = "Get email template by ID", description = "Retrieves a specific email template by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email template found",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDto.class))),
        @ApiResponse(responseCode = "404", description = "Email template not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmailTemplateDto> getEmailTemplateById(
            @Parameter(description = "Email template ID", required = true)
            @PathVariable String id) {
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
    @Operation(summary = "Get all email templates", description = "Retrieves all email templates with optional filtering by name and type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of email templates retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<EmailTemplateDto>> getAllEmailTemplates(
            @Parameter(description = "Optional filter by template name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Optional filter by email type")
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
    @Operation(summary = "Update email template", description = "Updates an existing email template with new details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email template updated successfully",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDto.class))),
        @ApiResponse(responseCode = "404", description = "Email template not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmailTemplateDto> updateEmailTemplate(
            @Parameter(description = "Email template ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated email template details", required = true)
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
    @Operation(summary = "Delete email template", description = "Deletes an existing email template")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Email template deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Email template not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailTemplate(
            @Parameter(description = "Email template ID", required = true)
            @PathVariable String id) {
        emailTemplateService.deleteEmailTemplate(id);
        return ResponseEntity.noContent().build();
    }
} 