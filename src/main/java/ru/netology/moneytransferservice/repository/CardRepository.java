package ru.netology.moneytransferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.moneytransferservice.model.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
