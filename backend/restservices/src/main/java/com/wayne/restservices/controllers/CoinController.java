package com.wayne.restservices.controllers;

import com.wayne.entities.dto.CoinDto;
import com.wayne.restservices.services.CoinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping
    public List<CoinDto> getAllCoins() {
        return coinService.getAllCoins();
    }

    @GetMapping("/{id}")
    public CoinDto getCoin(@PathVariable Long id) {
        return coinService.getCoin(id);
    }
}
