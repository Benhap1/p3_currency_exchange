package ru.skillbox.currency.exchange.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.xml.ValCurs;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class CurrencyParserService {

    private final CurrencyRepository currencyRepository;

    @Value("${currency.url}")
    private String currencyUrl;

    @PostConstruct
    public void init() {
        try {
            parseAndSaveCurrencyData();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during initial data loading: " + e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Выполнять каждый час
    public void parseAndSaveCurrencyData() throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        URL url = new URL(currencyUrl);
        ValCurs valCurs = (ValCurs) jaxbUnmarshaller.unmarshal(url);

        valCurs.getValutes().forEach(valute -> {
            Currency currency = new Currency();
            currency.setName(valute.getName());
            currency.setNominal(Long.parseLong(valute.getNominal()));
            currency.setValue(Double.parseDouble(valute.getValue().replace(',', '.')));
            currency.setIsoNumCode(Long.parseLong(valute.getNumCode()));
            currency.setIsoCharCode(valute.getCharCode());

            currencyRepository.save(currency);
        });
    }
}
