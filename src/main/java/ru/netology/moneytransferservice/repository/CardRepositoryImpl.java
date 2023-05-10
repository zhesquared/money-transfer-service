package ru.netology.moneytransferservice.repository;

import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Card;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardRepositoryImpl implements CardRepository {

    private final Map<String, Card> cardsData = new ConcurrentHashMap<>();

    {
        cardsData.put("4875131749697170",
                new Card("4875131749697170", "12/23", "498", "Shcherbakov", "Mikhail",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(15220, "RUR"))
                        )));
        cardsData.put("4474958586817833",
                new Card("4474958586817833", "09/27", "781", "Blinova", "Milana",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(3200, "RUR"))
                        )));
        cardsData.put("4935478279404040",
                new Card("4935478279404040", "08/24", "695", "Evdokimov", "Anton",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(95325, "RUR"))
                        )));
        cardsData.put("4557530545127271",
                new Card("4557530545127271", "09/25", "676", "Ryzhov", "Timur",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(945, "RUR"))
                        )));
        cardsData.put("4255043989582915",
                new Card("4255043989582915", "01/28", "660", "Sokolova", "Polina",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(9812, "RUR"))
                        )));
        cardsData.put("5355234686061156",
                new Card("5355234686061156", "08/24", "857", "Vasileva", "Kseniya",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(321548, "RUR"))
                        )));
        cardsData.put("5509190158858294",
                new Card("5509190158858294", "05/26", "409", "Zuev", "Demid",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(2154, "RUR"))
                        )));
        cardsData.put("5224855342102882",
                new Card("5224855342102882", "04/28", "539", "Avdeev", "Yaroslav",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(9841, "RUR"))
                        )));
        cardsData.put("5561903268892226",
                new Card("5561903268892226", "08/23", "207", "Kozlova", "Mariya",
                        new ConcurrentHashMap<>(
                                Map.of("RUR", new Amount(205, "RUR"))
                        )));
    }

    @Override
    public Optional<Card> getCardByNumber(String cardNumber) {
        return Optional.ofNullable(cardsData.get(cardNumber));
    }
}