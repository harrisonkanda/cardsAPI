package com.logicea.logiceacardsproject.repository;

import com.logicea.logiceacardsproject.model.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardModel, UUID>, JpaSpecificationExecutor<CardModel> {
    Optional<CardModel> findByName(String name);
}
