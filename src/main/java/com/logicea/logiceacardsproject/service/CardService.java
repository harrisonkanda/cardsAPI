package com.logicea.logiceacardsproject.service;

import com.logicea.logiceacardsproject.dto.request.CardRequestDto;
import com.logicea.logiceacardsproject.dto.response.CardResponseDto;
import com.logicea.logiceacardsproject.enums.CardStatus;
import com.logicea.logiceacardsproject.exception.AccessDeniedException;
import com.logicea.logiceacardsproject.exception.DataNotFoundException;
import com.logicea.logiceacardsproject.model.CardModel;
import com.logicea.logiceacardsproject.model.Specification.CardSpecification;
import com.logicea.logiceacardsproject.model.UserModel;
import com.logicea.logiceacardsproject.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.logicea.logiceacardsproject.util.CommonUtil.hasAdminRole;

@Service
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final UserService userService;

    @Autowired
    public CardService(final CardRepository cardRepository, final UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
    }

    public Optional<CardModel> findByName(final String name) {
        return cardRepository.findByName(name);
    }

    public CardResponseDto findByCardId(final UUID cardId, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        CardModel cardModel = cardRepository.findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("No Card Found with a matching cardId[=" + cardId));

        if (!hasAdminRole(authentication) && !userDetails.getUsername().equalsIgnoreCase(cardModel.getOwner().getEmail())) {
            throw new AccessDeniedException("You do not have access to view this card [id=" + cardId);
        }

        CardResponseDto cardResponseDto = CardResponseDto.builder()
                .cardId(cardModel.getCardId())
                .name(cardModel.getName())
                .description(cardModel.getDescription())
                .date_created(cardModel.getDateCreated())
                .color(cardModel.getColor())
                .build();

        return cardResponseDto;
    }

    public CardResponseDto updateCardById(final UUID cardId, CardRequestDto cardRequestDto, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        CardModel cardModel = cardRepository.findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("No Card Found with a matching cardId[=" + cardId));

        if (!hasAdminRole(authentication) && !userDetails.getUsername().equalsIgnoreCase(cardModel.getOwner().getEmail())) {
            throw new AccessDeniedException("You do not have access to view this card [id=" + cardId);
        }

        cardModel.setName(cardRequestDto.getName());
        cardModel.setDescription(cardRequestDto.getDescription());
        cardModel.setColor(cardRequestDto.getColor());
        if (cardRequestDto.getStatus().name() != null) {
            cardModel.setStatus(cardRequestDto.getStatus());
        }

        CardModel updatedCard = cardRepository.save(cardModel);

        return CardResponseDto.builder()
                .name(updatedCard.getName())
                .description(updatedCard.getDescription())
                .color(updatedCard.getColor())
                .date_created(updatedCard.getDateCreated())
                .cardId(updatedCard.getCardId())
                .build();
    }

    public void deleteCardById(final UUID cardId, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        CardModel cardModel = cardRepository.findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("No Card Found with a matching cardId[=" + cardId));

        if (!hasAdminRole(authentication) && !userDetails.getUsername().equalsIgnoreCase(cardModel.getOwner().getEmail())) {
            throw new AccessDeniedException("You do not have access to view this card [id=" + cardId);
        }

        cardRepository.deleteById(cardId);
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public void save(CardRequestDto cardRequestDto, final String email) {

        Optional<UserModel> user = userService.findUserByEmail(email);

        CardModel card = CardModel.builder()
                .name(cardRequestDto.getName())
                .description(cardRequestDto.getDescription())
                .color(cardRequestDto.getColor())
                .owner(user.get())
                .build();

        cardRepository.save(card);
    }

    public Page<CardResponseDto> filterCardsBySpecification(String name, String color, CardStatus status,
                                                            Date dateCreated, UUID user_id, boolean isAdmin, Pageable pageable) {

        Page<CardModel> cardModels = cardRepository.findAll(
                CardSpecification.withName(name)
                        .and(CardSpecification.withColor(color))
                        .and(CardSpecification.withStatus(status))
                        .and(CardSpecification.withDateCreated(dateCreated))
                        .and(CardSpecification.withUserId(user_id, isAdmin)),
                pageable
        );

        return new PageImpl<>(cardModels.stream()
                .map(element -> {
                    CardResponseDto card = CardResponseDto.builder()
                            .name(element.getName())
                            .description(element.getDescription())
                            .color(element.getColor())
                            .date_created(element.getDateCreated())
                            .cardId(element.getCardId())
                            .build();

                    return card;
                }).collect(Collectors.toList()), cardModels.getPageable(), cardModels.getTotalElements());
    }
}
