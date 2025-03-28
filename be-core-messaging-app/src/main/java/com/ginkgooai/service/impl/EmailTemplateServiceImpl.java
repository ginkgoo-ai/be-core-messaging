package com.ginkgooai.service.impl;

import com.ginkgooai.core.common.exception.GinkgooRunTimeException;
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

    /**
     * 将EmailTemplate实体转换为DTO
     */
    private EmailTemplateDto convertToDto(EmailTemplate emailTemplate) {
        return EmailTemplateDto.builder()
                .id(emailTemplate.getId())
                .name(emailTemplate.getName())
                .description(emailTemplate.getDescription())
                .emailType(emailTemplate.getEmailType())
                .subject(emailTemplate.getSubject())
                .content(emailTemplate.getContent())
                .properties(emailTemplate.getProperties())
                .build();
    }

    /**
     * 将DTO转换为EmailTemplate实体
     */
    private EmailTemplate convertToEntity(EmailTemplateDto emailTemplateDto) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setName(emailTemplateDto.getName());
        emailTemplate.setDescription(emailTemplateDto.getDescription());
        emailTemplate.setEmailType(emailTemplateDto.getEmailType());
        emailTemplate.setSubject(emailTemplateDto.getSubject());
        emailTemplate.setContent(emailTemplateDto.getContent());
        emailTemplate.setProperties(emailTemplateDto.getProperties());
        return emailTemplate;
    }

    @Override
    @Transactional
    public EmailTemplateDto createEmailTemplate(EmailTemplateDto emailTemplateDto) {
        // 检查是否已存在相同名称的模板
        if (emailTemplateRepository.existsByName(emailTemplateDto.getName())) {
            throw new ResourceDuplicatedException("Email Template", "name", emailTemplateDto.getName());
        }
        
        EmailTemplate emailTemplate = convertToEntity(emailTemplateDto);
        EmailTemplate savedTemplate = emailTemplateRepository.save(emailTemplate);
        return convertToDto(savedTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateDto getEmailTemplateById(String id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email Template", "id", id));
        return convertToDto(emailTemplate);
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
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmailTemplateDto updateEmailTemplate(String id, EmailTemplateDto emailTemplateDto) {
        // 检查模板是否存在
        EmailTemplate existingTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email Template", "id", id));
        
        // 如果更改了名称，检查新名称是否与其他模板冲突
        if (!existingTemplate.getName().equals(emailTemplateDto.getName()) && 
                emailTemplateRepository.existsByName(emailTemplateDto.getName())) {
            throw new ResourceDuplicatedException("Email Template", "name", emailTemplateDto.getName());
        }
        
        // 更新模板属性
        existingTemplate.setName(emailTemplateDto.getName());
        existingTemplate.setDescription(emailTemplateDto.getDescription());
        existingTemplate.setEmailType(emailTemplateDto.getEmailType());
        existingTemplate.setSubject(emailTemplateDto.getSubject());
        existingTemplate.setContent(emailTemplateDto.getContent());
        existingTemplate.setProperties(emailTemplateDto.getProperties());
        
        EmailTemplate updatedTemplate = emailTemplateRepository.save(existingTemplate);
        return convertToDto(updatedTemplate);
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