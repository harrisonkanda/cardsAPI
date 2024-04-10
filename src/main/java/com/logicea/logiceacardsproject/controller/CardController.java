package com.logicea.logiceacardsproject.controller;

import static com.logicea.logiceacardsproject.constants.ConfigurationConstant.*;

import com.logicea.logiceacardsproject.dto.request.CardRequestDto;
import com.logicea.logiceacardsproject.dto.request.filters.CardFilterRequest;
import com.logicea.logiceacardsproject.dto.request.filters.PaginationFilterRequest;
import com.logicea.logiceacardsproject.dto.response.CardResponseDto;
import com.logicea.logiceacardsproject.enums.Role;
import com.logicea.logiceacardsproject.exception.CardNameAlreadyTakenException;
import com.logicea.logiceacardsproject.model.UserModel;
import com.logicea.logiceacardsproject.service.CardService;
import com.logicea.logiceacardsproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Cards-API", description = "CARD CRUD OPERATION")
@RequestMapping(API_VERSION_URL + CARD_API_URI)
@RestController
public class CardController {

    private CardService cardService;
    private UserService userService;

    @Autowired
    public CardController(final CardService cardService, final UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @Operation(summary = "Create a Card", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = CardResponseDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema())})
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody CardRequestDto cardRequestDto,
                                 Authentication authentication, BindingResult result) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (cardService.findByName(cardRequestDto.getName()).isPresent()) {
            throw new CardNameAlreadyTakenException("Card name must be unique. Name Already taken");
        }

        cardService.save(cardRequestDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Filter Cards", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CardResponseDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema())})
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CardResponseDto>> filterCards(@ModelAttribute CardFilterRequest cardFilterRequest,
                                                             @ModelAttribute PaginationFilterRequest paginationFilterRequest,
                                                             Authentication authentication, BindingResult result) {


        UserDetails userDetails = ((UserDetails) authentication.getPrincipal());
        UserModel userModel = userService.findUserByEmail(userDetails.getUsername()).get();
        boolean isAdmin = userModel.getRole().equals(Role.ADMIN) ? true : false;

        Sort.Direction direction = null;
        String sortBy = "";

        if (paginationFilterRequest.getSortBy() != "") {
            sortBy = paginationFilterRequest.getSortBy();
            direction = paginationFilterRequest.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        PageRequest pageRequest = PageRequest.of((paginationFilterRequest.getPage() > 0 ? paginationFilterRequest.getPage() : 0),
                paginationFilterRequest.getPageSize(),
                Sort.by(direction, sortBy)
        );

        Page<CardResponseDto> paginatedCards = cardService.filterCardsBySpecification(
                cardFilterRequest.getName(),
                cardFilterRequest.getColor(),
                cardFilterRequest.getStatus(),
                cardFilterRequest.getDateCreated(),
                userModel.getUserId(),
                isAdmin,
                pageRequest
        );
        return ResponseEntity.ok(paginatedCards);
    }

    @Operation(summary = "Fetch Card By CardId", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CardResponseDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema())})
    })
    @GetMapping(value = "/{cardId}")
    public ResponseEntity<CardResponseDto> findCardById(@PathVariable(value = "cardId", required = true) UUID cardId,
                                                        Authentication authentication) {
        return ResponseEntity.ok().body(cardService.findByCardId(cardId, authentication));
    }

    @Operation(summary = "Update Card By CardId", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CardResponseDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema())})
    })
    @PutMapping(value = "/{cardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardResponseDto> update(@PathVariable(value = "cardId", required = true) UUID cardId,
                                                  @Valid @RequestBody CardRequestDto cardRequestDto,
                                                  Authentication authentication, BindingResult result) {

        return ResponseEntity.ok().body(cardService.updateCardById(cardId, cardRequestDto, authentication));
    }

    @Operation(summary = "Delete Card By CardId", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable(value = "cardId", required = true) UUID cardId,
                                 Authentication authentication) {

        cardService.deleteCardById(cardId, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}