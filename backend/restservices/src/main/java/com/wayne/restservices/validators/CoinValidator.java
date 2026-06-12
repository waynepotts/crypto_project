package com.wayne.restservices.validators;

import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.exceptions.CoinAlreadyExistsException;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.services.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CoinValidator {

    private final CoinRepository coinRepository;

    private static final Logger log =
            LoggerFactory.getLogger(CoinValidator.class);
    public CoinValidator(
            CoinRepository coinRepository
    ) {
        this.coinRepository = coinRepository;
    }

    public void validateCreateCoin(
            CreateCoinRequestDto request
    ) {

        validateCoingeckoIdUnique(
                request.getCoingeckoId()
        );

        validateSymbolUnique(
                request.getSymbol()
        );
    }
    public void validateUpdateCoin(
            UpdateCoinRequestDto request
    ) {
        validateCoinIdExists(request.getId());
        validateCoingeckoIdUnique(
                request.getCoingeckoId()
        );

        validateSymbolUnique(
                request.getSymbol()
        );
    }

    private void validateCoingeckoIdUnique(
            String coingeckoId
    ) {

        if (coinRepository.existsByCoingeckoId(
                coingeckoId
        )) {

            throw new CoinAlreadyExistsException(
                    coingeckoId
            );
        }
    }

    private void validateSymbolUnique(
            String symbol
    ) {

        if (coinRepository.existsBySymbolIgnoreCase(
                symbol
        )) {

            throw new CoinAlreadyExistsException(symbol
            );
        }
    }

    private void validateCoinIdExists(Long id){
        if (id == null || !coinRepository.existsById(id)) {
            log.error("Coin not found {}", id);
            throw new CoinNotFoundException(id);

        }
    }
}