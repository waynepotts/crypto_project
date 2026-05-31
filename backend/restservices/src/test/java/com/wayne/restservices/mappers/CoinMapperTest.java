package com.wayne.restservices.mappers;

import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.entities.jpa.Coin;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CoinMapperTest {

    @Test
    void shouldMapCoinToDto() {
        Coin coin = createCoin();

        CoinResponseDto dto = CoinMapper.toDto(coin);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("coingeckoId_BTC", dto.coingeckoId());
        assertEquals("Bitcoin", dto.name());
        assertEquals("BTC", dto.symbol());
        assertEquals("bitcoin.png", dto.image());
    }

    @Test
    void shouldMapCreateRequestToEntity() {
        CreateCoinRequestDto request = new CreateCoinRequestDto();
        request.setCoingeckoId("ethereum");
        request.setName("Ethereum");
        request.setSymbol("ETH");
        request.setImage("ethereum.png");

        Coin coin = CoinMapper.toEntity(request);

        assertNotNull(coin);
        assertNull(coin.getId());
        assertEquals("ethereum", coin.getCoingeckoId());
        assertEquals("Ethereum", coin.getName());
        assertEquals("ETH", coin.getSymbol());
        assertEquals("ethereum.png", coin.getImage());
    }

    @Test
    void shouldMapResponseDtoToEntity() {
        CoinResponseDto dto = new CoinResponseDto(1L, "bitcoin", "BTC", "Bitcoin", "bitcoin.png", null);

        Coin coin = CoinMapper.toEntity(dto);

        assertNotNull(coin);
        assertEquals(1L, coin.getId());
        assertEquals("bitcoin", coin.getCoingeckoId());
        assertEquals("Bitcoin", coin.getName());
        assertEquals("BTC", coin.getSymbol());
        assertEquals("bitcoin.png", coin.getImage());
    }

    @Test
    void shouldMapUpdateRequestToEntity() {
        UpdateCoinRequestDto request = new UpdateCoinRequestDto();
        request.setId(1L);
        request.setCoingeckoId("bitcoin-updated");
        request.setName("Bitcoin Updated");
        request.setSymbol("BTC");
        request.setImage("bitcoin_updated.png");

        Coin coin = CoinMapper.toEntity(request);

        assertNotNull(coin);
        assertEquals(1L, coin.getId());
        assertEquals("bitcoin-updated", coin.getCoingeckoId());
        assertEquals("Bitcoin Updated", coin.getName());
        assertEquals("BTC", coin.getSymbol());
        assertEquals("bitcoin_updated.png", coin.getImage());
    }

    @Test
    void shouldReturnNullWhenCoinIsNull() {
        CoinResponseDto dto = CoinMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void shouldReturnTrueWhenCoinAndDtoAreEqual() {
        Coin coin = createCoin();
        CoinResponseDto dto = CoinMapper.toDto(coin);

        assertTrue(CoinMapper.equals(coin, dto));
    }

    @Test
    void shouldReturnFalseWhenCoinAndDtoAreNotEqual() {
        Coin coin = createCoin();
        CoinResponseDto dto = new CoinResponseDto(coin.getId(), coin.getCoingeckoId(), coin.getSymbol(), "Different", coin.getImage(), null);

        assertFalse(CoinMapper.equals(coin, dto));
    }

    @Test
    void shouldReturnFalseWhenCoinOrDtoIsNull() {
        Coin coin = createCoin();
        assertFalse(CoinMapper.equals(null, new CoinResponseDto(null, null, null, null, null, null)));
        assertFalse(CoinMapper.equals(coin, null));
        assertFalse(CoinMapper.equals(null, null));
    }

    private Coin createCoin() {
        Coin coin = new Coin();
        coin.setId(1L);
        coin.setCoingeckoId("coingeckoId_BTC");
        coin.setName("Bitcoin");
        coin.setSymbol("BTC");
        coin.setImage("bitcoin.png");
        return coin;
    }
}
