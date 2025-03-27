package com.ginkgooai.repository.specification;

import com.ginkgooai.domain.EmailTemplate;
import com.ginkgooai.domain.EmailType;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Email Template Specification for dynamic queries
 */
public class EmailTemplateSpecification {
    
    /**
     * Create specification for filtering email templates
     *
     * @param name optional name filter
     * @param emailType optional email type filter
     * @return specification with combined filters
     */
    public static Specification<EmailTemplate> filterBy(String name, EmailType emailType) {
        return (Root<EmailTemplate> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Add name filter if provided
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.equal(root.get("name"), name));
            }
            
            // Add email type filter if provided
            if (emailType != null) {
                predicates.add(cb.equal(root.get("emailType"), emailType));
            }
            
            // Combine all predicates with AND operator
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 