package com.wayne.restservices.controllers;

import com.wayne.restservices.dtos.*;
import com.wayne.restservices.services.CoinMarketDataService;
import com.wayne.restservices.services.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
@Tag(name = "Coins", description = "Coin management APIs")
public class CoinController {

    private final CoinService coinService;
    private final CoinMarketDataService coinMarketDataService;

    public CoinController(CoinService coinService, CoinMarketDataService coinMarketDataService) {
        this.coinService = coinService;
        this.coinMarketDataService = coinMarketDataService;
    }

    @Operation(summary = "Returns coins in pages")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "all coins types in the DB"
            )
    })
    @GetMapping()
    public PagedResponseDto<CoinResponseDto> getCoins(
            @PageableDefault(
                    size = 20,
                    sort = "name"
            )
            Pageable pageable
    ) {

        return coinService.getCoins(pageable);
    }

    @Operation(summary = "Returns all coins")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "all coins types in the DB"
            )
    })
    @GetMapping("/all")
    public List<CoinResponseDto> getAllCoins() {
        return coinService.getAllCoins();
    }

    @Operation(summary = "Get coin by ID")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Coin found"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Coin not found"
            )
    })
    @GetMapping("/{id}")
    public CoinResponseDto getCoin(@PathVariable Long id) {
        return coinService.getCoin(id);
    }

    @Operation(summary = "Create coin")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Coin created"
            )
    })
    @PostMapping
    public CoinResponseDto createCoin(
            @Valid @RequestBody CreateCoinRequestDto request
    ) {
        return coinService.createCoin(request);
    }

    @Operation(summary = "Update coin")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Coin updated"
            )
    })
    @PutMapping
    public CoinResponseDto updateCoin(@Valid @RequestBody UpdateCoinRequestDto request) {
        return coinService.updateCoin(request);
    }

    @Operation(summary = "Paged coin history")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Page of history"
            )
    })

    @GetMapping("/{id}/history")
    public CoinHistoryPagedResponseDto getCoinHistory(@PathVariable Long id, Long from, Long to,
                                                      Integer page, Integer pageSize) {
        Instant fromInstant = Instant.ofEpochSecond(from);
        Instant toInstant = Instant.ofEpochSecond(to);
        return coinMarketDataService.getCoinHistory(id,fromInstant, toInstant, page, pageSize);
    }

    @GetMapping("/{id}/history_chart")
    public CoinHistoryResponseDto getHistoryChart(@PathVariable Long id, Integer days, @RequestParam(defaultValue = "false") Boolean daily ) {
        return coinMarketDataService.getChartData(id, days, daily);
    }
    @Operation(summary = "Search coins by name or symbol (partial, case-insensitive)")
    @GetMapping("/search")
    public PagedResponseDto<CoinResponseDto> searchCoins(
            @RequestParam String q,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return coinService.searchCoins(q, pageable);
    }
}
