package ru.netology.moneytransferservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.netology.moneytransferservice.domain.Card;

public interface NewCardRepository extends CrudRepository<Card, Long> {

    Card findByCardNumber(String cardNumber);
}
