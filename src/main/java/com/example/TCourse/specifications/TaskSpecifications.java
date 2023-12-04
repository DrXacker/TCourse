package com.example.TCourse.specifications;

import com.example.TCourse.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class TaskSpecifications {

    public static Specification<Task> searchTasksAllRows(String username, String body, String priority, String category,
                                                  LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.hasText(username)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("username"), username));
            }

            if (StringUtils.hasText(body)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("body"), "%" + body + "%"));
            }

            if (priority != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("priority"), priority));
            }

            if (category != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category"), category));
            }

            if (startDateTime != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("startDateTime"), startDateTime));
            }

            if (endDateTime != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("endDateTime"), endDateTime));
            }

            return predicate;
        };
    }
}
