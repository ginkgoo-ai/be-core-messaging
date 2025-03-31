package com.ginkgooai.repository;

import com.ginkgooai.domain.EmailTemplate;
import com.ginkgooai.domain.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, String>, JpaSpecificationExecutor<EmailTemplate> {
    
    /**
     * Find email template by name
     *
     * @param name template name
     * @return email template
     */
    Optional<EmailTemplate> findByName(String name);
    
    /**
     * Find email templates by type
     *
     * @param emailType email type
     * @return list of email templates
     */
    List<EmailTemplate> findByEmailType(EmailType emailType);
    
    /**
     * Check if a template with the given name exists
     *
     * @param emailType template type
     * @return true if exists, false otherwise
     */
    boolean existsByEmailType(EmailType emailType);
} 