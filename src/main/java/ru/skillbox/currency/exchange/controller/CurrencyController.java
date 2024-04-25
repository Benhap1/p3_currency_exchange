package ru.skillbox.currency.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.service.CurrencyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/currency")
public class CurrencyController {
    private final CurrencyService service;

    @GetMapping(value = "/{id}")
    ResponseEntity<CurrencyDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/convert")
    ResponseEntity<Double> convertValue(@RequestParam("value") Long value, @RequestParam("numCode") Long numCode) {
        return ResponseEntity.ok(service.convertValue(value, numCode));
    }

    @PostMapping("/create")
    ResponseEntity<CurrencyDto> create(@RequestBody CurrencyDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getAllCurrencies() {
        // Получаем список всех валют с помощью сервиса
        List<CurrencyDto> currencies = service.getAllCurrencies();

        // Преобразуем список валют в необходимый формат
        List<Map<String, Object>> formattedCurrencies = currencies.stream()
                .map(currency -> {
                    // Создаем отображение для каждой валюты, содержащее только поля "name" и "value"
                    Map<String, Object> formattedCurrency = new HashMap<>();
                    formattedCurrency.put("name", currency.getName());
                    formattedCurrency.put("value", currency.getValue());
                    return formattedCurrency;
                })
                .collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("currencies", formattedCurrencies);

        return ResponseEntity.ok(response);
    }

}