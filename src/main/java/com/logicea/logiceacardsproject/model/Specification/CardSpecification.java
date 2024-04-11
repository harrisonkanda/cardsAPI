package com.logicea.logiceacardsproject.model.Specification;

import com.logicea.logiceacardsproject.enums.CardStatus;
import com.logicea.logiceacardsproject.model.CardModel;
import org.springframework.data.jpa.domain.Specification;
import java.util.Date;
import java.util.UUID;

public class CardSpecification {

    public static Specification<CardModel> withName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),  "%"+ name.toLowerCase() +"%");
        };
    }

    public static Specification<CardModel> withColor(String color) {
        return (root, query, criteriaBuilder) -> {
            if (color == null || color.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("color")), "%"+ color.toLowerCase() +"%");
        };
    }

    public static Specification<CardModel> withStatus(CardStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<CardModel> withDateCreated(Date dateCreated) {
        return (root, query, criteriaBuilder) -> {
            if (dateCreated == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("dateCreated"), dateCreated);
        };
    }

    public static Specification<CardModel> withUserId(UUID userId, boolean isAdmin) {
        return ((root, query, criteriaBuilder) -> {
            if (isAdmin) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("owner").get("userId"), userId);
            }
        });
    }
}
