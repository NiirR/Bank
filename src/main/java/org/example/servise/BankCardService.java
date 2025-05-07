package org.example.servise;

import lombok.RequiredArgsConstructor;
import org.example.entity.BankCard;
import org.example.entity.CardStatus;
import org.example.entity.EncryptionUtil;
import org.example.entity.User;
import org.example.repository.BankCardRepository;
import org.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BankCardService{

    private final BankCardRepository bankCardRepository;
    private final UserRepository userRepository;

    public BankCard createCard(BankCard card) {
        String lastFourDigits = card.getCardNumber().substring(card.getCardNumber().length() - 4);
        String encryptedCardNumber = EncryptionUtil.encode(card.getCardNumber());
        card.setCardNumber(encryptedCardNumber);


        card.setCardNumberMasked("**** **** **** " + lastFourDigits);

        User user = card.getUser();
        card.setUser(user);
        card.setCardHolder(user.getName());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        card.setExpirationDate(LocalDate.now().plusYears(3));

        return bankCardRepository.save(card);
    }

    public List<BankCard> getAllUsersCards(Long userId) {
        return bankCardRepository.findAllBankCardsByUserId(userId);
}

    public Page<BankCard> getAllCards(Pageable pageable) {
        return bankCardRepository.findAll(pageable);
    }

    public BankCard getCardById(Long id){
        return bankCardRepository.findBankCardById(id);
    }

    public BankCard updateCard(Long id, BankCard cardDetails) {
        BankCard card = getCardById(id);

        card.setExpirationDate(cardDetails.getExpirationDate());
        card.setStatus(cardDetails.getStatus());
        card.setBalance(cardDetails.getBalance());

        return bankCardRepository.save(card);
    }

    public void deleteCard(Long id) {
        BankCard card = getCardById(id);
        bankCardRepository.delete(card);
    }

    public BankCard blockCard(Long id) {
        BankCard card = getCardById(id);
        card.setStatus(CardStatus.BLOCKED);
        return bankCardRepository.save(card);
    }

    public BankCard activateCard(Long id) {
        BankCard card = getCardById(id);
        card.setStatus(CardStatus.ACTIVE);
        return bankCardRepository.save(card);
    }
}
