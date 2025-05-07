package org.example.repository;

import org.example.entity.BankCard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {

    List<BankCard> findAllBankCardsByUserId(Long userId);

    Page<BankCard> findAll(Pageable pageable);

    BankCard findBankCardById(Long id);

}
