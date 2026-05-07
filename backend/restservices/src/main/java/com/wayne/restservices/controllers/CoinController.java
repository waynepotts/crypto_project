package com.wayne.restservices.controllers;

import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.services.CoinService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping
    public List<CoinResponseDto> getAllCoins() {
        return coinService.getAllCoins();
    }

    @GetMapping("/{id}")
    public CoinResponseDto getCoin(@PathVariable Long id) {
        return coinService.getCoin(id);
    }

    @PostMapping
    public CoinResponseDto createCoin(
            @Valid @RequestBody CreateCoinRequestDto request
    ) {
        return coinService.createCoin(request);
    }
}
