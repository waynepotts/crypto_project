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
        assertEquals(1L, dto.getId());
        assertEquals("coingeckoId_BTC", dto.getCoingeckoId());
        assertEquals("Bitcoin", dto.getName());
        assertEquals("BTC", dto.getSymbol());
        assertEquals("bitcoin.png", dto.getImage());
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
        CoinResponseDto dto = new CoinResponseDto();
        dto.setId(1L);
        dto.setCoingeckoId("bitcoin");
        dto.setName("Bitcoin");
        dto.setSymbol("BTC");
        dto.setImage("bitcoin.png");

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
        CoinResponseDto dto = CoinMapper.toDto(coin);
        dto.setName("Different");

        assertFalse(CoinMapper.equals(coin, dto));
    }

    @Test
    void shouldReturnFalseWhenCoinOrDtoIsNull() {
        Coin coin = createCoin();
        assertFalse(CoinMapper.equals(null, new CoinResponseDto()));
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
