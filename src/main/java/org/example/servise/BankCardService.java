package org.example.servise;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.entity.BankCard;
import org.example.entity.CardStatus;
import org.example.entity.User;
import org.example.repository.BankCardRepository;
import org.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class BankCardServiceImpl {

    private final BankCardRepository bankCardRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;


    public BankCardDto createCard(Long userId, CreateCardDto createCardDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        BankCard card = new BankCard();
        card.setUser(user);
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
        }

        BankCard savedCard = bankCardRepository.save(card);
        return bankCardMapper.toDto(savedCard);
    }

    @Override
    public BankCardDto getCardById(Long userId, Long cardId) {
        BankCard card = bankCardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card not found with id: " + cardId + " for user: " + userId));
        return bankCardMapper.toDto(card);
    }


    public getUserCards(Long userId, Pageable pageable) {
        return bankCardRepository.findByUserId(userId, pageable)
                .map(bankCardMapper::toDto);
    }


    public getUserCardsByStatus(Long userId, CardStatus status, Pageable pageable) {
        return null;
    }

    @Override
    public BankCardDto updateCardStatus(Long userId, Long cardId, CardStatus newStatus) {
        BankCard card = bankCardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card not found with id: " + cardId + " for user: " + userId));

        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new RuntimeException("Cannot change status of an expired card");
        }

        card.setStatus(newStatus);
        BankCard updatedCard = bankCardRepository.save(card);
        return bankCardMapper.toDto(updatedCard);
    }

    @Override
    public void deleteCard(Long userId, Long cardId) {

    }

    @Override
    public TransactionDto transferBetweenCards(Long userId, TransferRequestDto transferRequest) {
        if (transferRequest.getFromCardId().equals(transferRequest.getToCardId())) {
            throw new RuntimeException("Cannot transfer to the same card");
        }

        BankCard fromCard = bankCardRepository.findByIdAndUserId(transferRequest.getFromCardId(), userId)
                .orElseThrow(() -> new EntityNotFoundException ("Card not found with id: " + transferRequest.getFromCardId()
                        + "Card not found with id: " + transferRequest.getFromCardId() + " for user: " + userId));

        BankCard toCard = bankCardRepository.findById(transferRequest.getToCardId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card not found with id: " + transferRequest.getToCardId()));

        if (fromCard.getStatus() != CardStatus.ACTIVE || toCard.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException("Both cards must be active for transfer");
        }

        if (fromCard.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds on the source card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(transferRequest.getAmount()));
        toCard.setBalance(toCard.getBalance().add(transferRequest.getAmount()));

        bankCardRepository.save(fromCard);
        bankCardRepository.save(toCard);

        return transactionService.createTransaction(userId, transferRequest);
    }

    @Override
    public Page<BankCardDto> searchCards(Long userId, String searchTerm, Pageable pageable) {
        return null;
    }

}
