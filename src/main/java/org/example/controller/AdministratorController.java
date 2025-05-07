package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.BankCard;
import org.example.servise.BankCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdministratorController {
    private final BankCardService bankCardService;

    @GetMapping("/cards")
    public ResponseEntity<Page<BankCard>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<BankCard> cards = bankCardService.getAllCards(pageable);

        return ResponseEntity.ok(cards);
    }

    @DeleteMapping("/card/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        bankCardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/card_block/{id}")
    public ResponseEntity<BankCard> blockCard(@PathVariable Long id) {
        return ResponseEntity.ok(bankCardService.blockCard(id));
    }

    @PostMapping("/card_activ/{id}")
    public ResponseEntity<BankCard> activateCard(@PathVariable Long id) {
        return ResponseEntity.ok(bankCardService.activateCard(id));
    }

    @PostMapping("/addcard")
    public ResponseEntity<BankCard> createCard(@ModelAttribute("bank_cards") BankCard bankCard) {
        return ResponseEntity.ok(bankCardService.createCard(bankCard));
    }
}
