package com.wayne.restservices.services;

import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.dtos.PagedResponseDto;
import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCategoryDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDetailsDto;
import com.wayne.restservices.entities.jpa.Category;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.repositories.CategoryRepository;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.mappers.CoinMapper;
import com.wayne.restservices.validators.CoinValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.ofNullable;

@Service
public class CoinService {

    private final CoinRepository coinRepository;
    private final CoinValidator coinValidator;
    private final CoinGeckoClient coinGeckoClient;
    private final CategoryRepository categoryRepository;
    private static final Logger log =
            LoggerFactory.getLogger(CoinService.class);

    public List<CoinResponseDto> refreshCoins() {
        log.info("Starting Category refresh");
        List<CoinGeckoCategoryDto> cats = coinGeckoClient.getCategoryDtos();
        /*for (CoinGeckoCategoryDto cat : cats) {
            Category c = Optional.ofNullable(categoryRepository.findByCoingeckoId(cat.categoryId())).orElse(new Category());
            c.setName(cat.name());
            c.setCoingeckoCategoryId(cat.categoryId());
            categoryRepository.save(c);
            log.info("Category save "+ cat.name());
        }*/
        log.info("Starting CoinGecko refresh");
        List<CoinResponseDto> updatedCoins = new ArrayList<>();
        try {
            int count = 10;
            List<Coin> allCoins = coinRepository.findAll();
            for (Coin coin : allCoins) {
                if(count < 0){
                    break;
                }
                boolean updated = false;
                CoinGeckoCoinDetailsDto dto = coinGeckoClient.getCoinDetails(coin.getCoingeckoId());
                count--; // we don't want to spam coingecko with requests
                if(dto.categories().size() == coin.getCategories().size()){
                    continue;
                }
                Set<Category> catSet = new HashSet<>();
                for(String c: dto.categories()){
                    Category category = categoryRepository.findByName(c);
                    if(category != null){
                        updated = true;
                        catSet.add(category);
                    }
                    else{
                        log.info("Category not found " + c);
                    }
                }
                coin.setCategories(catSet);
                if(updated){

                    coinRepository.save(coin);
                    updatedCoins.add(CoinMapper.toDto(coin));
                }
            }
            log.info("Coin refresh completed successfully");

        } catch (Exception ex) {

            log.error("Failed to refresh coins", ex);

            throw ex;
        }
        return updatedCoins;
    }
    public CoinService(
            CoinRepository coinRepository,
            CoinValidator coinValidator, CoinGeckoClient coinGeckoClient, CategoryRepository categoryRepository
    ) {
        this.coinRepository = coinRepository;
        this.coinValidator = coinValidator;
        this.coinGeckoClient = coinGeckoClient;
        this.categoryRepository = categoryRepository;
    }

    public PagedResponseDto<CoinResponseDto> getCoins(Pageable pageable) {
        Page<CoinResponseDto> paged = coinRepository
                .findAll(pageable)
                .map(CoinMapper::toDto);
        return new PagedResponseDto<>(paged);
    }

    public List<CoinResponseDto> getAllCoins() {
        List<CoinResponseDto> allCoins;
        try {
            allCoins = coinRepository.findAll()
                    .stream()
                    .map(CoinMapper::toDto)
                    .toList();
        } catch (Exception ex) {
            log.error("Failed to to get all coins", ex);
            throw ex;
        }
        return allCoins;
    }

    public CoinResponseDto getCoin(Long id) {
        Coin coin = coinRepository.findById(id)
                .orElseThrow(() ->
                        new CoinNotFoundException(id));
        return CoinMapper.toDto(coin);
    }

    public CoinResponseDto createCoin(CreateCoinRequestDto request) {
        coinValidator.validateCreateCoin(request);
        Coin coin = CoinMapper.toEntity(request);
        Coin savedCoin =
                coinRepository.save(coin);
        return CoinMapper.toDto(savedCoin);
    }

    public CoinResponseDto updateCoin(UpdateCoinRequestDto updateCoin) {
        coinValidator.validateUpdateCoin(updateCoin);
        Coin coin = CoinMapper.toEntity(updateCoin);
        Coin savedCoin =
                coinRepository.save(coin);
        return CoinMapper.toDto(savedCoin);
    }

    public PagedResponseDto<CoinResponseDto> searchCoins(String query, Pageable pageable) {
        Page<CoinResponseDto> paged = coinRepository
                .findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(query, query, pageable)
                .map(CoinMapper::toDto);
        return new PagedResponseDto<>(paged);
    }
}
