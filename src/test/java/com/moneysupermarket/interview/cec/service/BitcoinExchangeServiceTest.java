package com.moneysupermarket.interview.cec.service;

import com.moneysupermarket.interview.cec.api.blockchain.api.BlockchainFeignClient;
import com.moneysupermarket.interview.cec.api.blockchain.dto.BlockchainPriceDto;
import com.moneysupermarket.interview.cec.api.exmo.api.ExmoFeignClient;
import com.moneysupermarket.interview.cec.api.exmo.dto.ExmoPriceDto;
import com.moneysupermarket.interview.cec.model.HighestPriceResponse;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BitcoinExchangeServiceTest {

    @Autowired
    private BitcoinExchangeService bitcoinExchangeService;

    @MockBean
    private BlockchainFeignClient blockchainFeignClient;

    @MockBean
    private ExmoFeignClient exmoFeignClient;

    @Test
    public void givenBlockchainHasTheHigherPrice_ThenBlockchainIsReturnedAsTheHighestPrice(){
        when(blockchainFeignClient.getCurrencies()).thenReturn(blockchainPriceOf(BigDecimal.valueOf(12999.99)));
        when(exmoFeignClient.getCurrencies()).thenReturn(exmoPriceOf(BigDecimal.valueOf(11999.99)));

        HighestPriceResponse highestPrice = bitcoinExchangeService.getHighestPrice();

        assertThat(highestPrice.getPrice()).isEqualTo(BigDecimal.valueOf(12999.99));
        assertThat(highestPrice.getApiName()).isEqualTo("Blockchain");
    }

    @Test
    public void givenExmoHasTheHigherPrice_ThenExmoIsReturnedAsTheHighestPrice(){
        when(blockchainFeignClient.getCurrencies()).thenReturn(blockchainPriceOf(BigDecimal.valueOf(12999.99)));
        when(exmoFeignClient.getCurrencies()).thenReturn(exmoPriceOf(BigDecimal.valueOf(13999.99)));

        HighestPriceResponse highestPrice = bitcoinExchangeService.getHighestPrice();

        assertThat(highestPrice.getPrice()).isEqualTo(BigDecimal.valueOf(13999.99));
        assertThat(highestPrice.getApiName()).isEqualTo("Exmo");
    }

    @Test
    public void givenUsdPriceCantBeFound_ThenAnExceptionIsThrown(){
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            bitcoinExchangeService.getHighestPrice();
        });
    }

    private Map<String, BlockchainPriceDto> blockchainPriceOf(BigDecimal price) {
        return Maps.newHashMap("USD", new BlockchainPriceDto(price));
    }

    private Map<String, ExmoPriceDto> exmoPriceOf(BigDecimal price) {
        return Maps.newHashMap("BTC_USD", new ExmoPriceDto(price));
    }
}

