package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.BankCard;
import org.example.servise.BankCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class BankCardController {
    private final BankCardService bankCardService;

    @GetMapping("/user_cards")
    public String bankCard(Model model , BankCard bankCard) {
        model.addAttribute("bankCards" , bankCard);
        return "create_card";
    }


    @GetMapping("/card/{id}")
    public ResponseEntity<BankCard> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(bankCardService.getCardById(id));
    }

    @PutMapping("/card/{id}")
    public ResponseEntity<BankCard> updateCard(@PathVariable Long id, @RequestBody BankCard bankCard) {
        return ResponseEntity.ok(bankCardService.updateCard(id, bankCard));
    }

    @GetMapping("/create_card")
    public String addUser(BankCard card , Model model) {
        model.addAttribute("card", card);
        return "create_card";
    }

}

