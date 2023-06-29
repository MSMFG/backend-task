package com.moneysupermarket.interview.cec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.moneysupermarket.interview.cec.model.HighestPriceResponse;
import com.moneysupermarket.interview.cec.service.BitcoinExchangeService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@WebMvcTest(BitcoinExchangeController.class)
public class BitcoinExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BitcoinExchangeService bitcoinExchangeService;

    private HighestPriceResponse highestPriceResponse;

    @Test
    public void getHighestCurrencyShouldCallGetCurrenciesService() throws Exception {
        when(bitcoinExchangeService.getHighestPrice()).thenReturn(highestPriceResponse);

        mockMvc.perform(get("/highestprice/")
                .contentType("application/json").accept("application/json"))
                .andExpect(status().isOk());

        verify(bitcoinExchangeService, VerificationModeFactory.times(1)).getHighestPrice();
        verifyNoMoreInteractions(bitcoinExchangeService);
    }
}