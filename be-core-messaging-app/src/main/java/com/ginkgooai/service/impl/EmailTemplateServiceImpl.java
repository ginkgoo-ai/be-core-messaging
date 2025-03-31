package com.ginkgooai.service.impl;

import com.ginkgooai.core.common.exception.ResourceDuplicatedException;
import com.ginkgooai.core.common.exception.ResourceNotFoundException;
import com.ginkgooai.domain.EmailTemplate;
import com.ginkgooai.domain.EmailType;
import com.ginkgooai.dto.EmailTemplateDto;
import com.ginkgooai.repository.EmailTemplateRepository;
import com.ginkgooai.repository.specification.EmailTemplateSpecification;
import com.ginkgooai.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件模板服务实现类
 */
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    @Transactional
    public EmailTemplateDto createEmailTemplate(EmailTemplateDto emailTemplateDto) {

        if (emailTemplateRepository.existsByType(emailTemplateDto.getEmailType())) {
            throw new ResourceDuplicatedException("Email Template", "type", emailTemplateDto.getName());
        }
        
        EmailTemplate emailTemplate = EmailTemplateDto.convertToEntity(emailTemplateDto);
        EmailTemplate savedTemplate = emailTemplateRepository.save(emailTemplate);
        return EmailTemplateDto.convertToDto(savedTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateDto getEmailTemplateById(String id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email Template", "id", id));
        return EmailTemplateDto.convertToDto(emailTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailTemplateDto> getAllEmailTemplates(String name, EmailType emailType) {
        // Create specification with the provided filters
        Specification<EmailTemplate> spec = EmailTemplateSpecification.filterBy(name, emailType);
        
        // Use specification to query the database
        List<EmailTemplate> templates = emailTemplateRepository.findAll(spec);
        
        // Convert entities to DTOs and return
        return templates.stream()
                .map(EmailTemplateDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmailTemplateDto updateEmailTemplate(String id, EmailTemplateDto emailTemplateDto) {
        EmailTemplate existingTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email Template", "id", id));
        
        if (!existingTemplate.getEmailType().equals(emailTemplateDto.getEmailType()) &&
                emailTemplateRepository.existsByType(emailTemplateDto.getEmailType())) {
            throw new ResourceDuplicatedException("Email Template", "name", emailTemplateDto.getName());
        }
        
        // 更新模板属性
        EmailTemplateDto.coverEmailTemplate(emailTemplateDto, existingTemplate);

        EmailTemplate updatedTemplate = emailTemplateRepository.save(existingTemplate);
        return EmailTemplateDto.convertToDto(updatedTemplate);
    }



    @Override
    @Transactional
    public void deleteEmailTemplate(String id) {
        if (!emailTemplateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Email Template", "id", id);
        }
        
        emailTemplateRepository.deleteById(id);
    }
} 