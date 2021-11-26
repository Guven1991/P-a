package com.vodafone.iotdevices.service;

import com.vodafone.iotdevices.dbmodel.SimCard;
import com.vodafone.iotdevices.dto.SimCardDto;
import com.vodafone.iotdevices.enums.SimCardStatusEnum;
import com.vodafone.iotdevices.exception.SimCardNotFoundException;
import com.vodafone.iotdevices.repository.SimCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SimCardServiceTest {

    @InjectMocks
    @Spy
    private SimCardService simCardService;

    @Mock
    private SimCardRepository cardRepository;

    private SimCard simCard;
    private SimCardDto simCardDto;

    @Before
    public void init() {
        simCard = SimCard.builder()
                .simID(1L)
                .operatorCode(555L)
                .countryName("ENGLAND")
                .simCardStatus(SimCardStatusEnum.READY)
                .build();

        simCardDto = SimCardDto.builder()
                .simID(simCard.getSimID())
                .operatorCode(simCard.getOperatorCode())
                .countryName(simCard.getCountryName())
                .simCardStatus(simCard.getSimCardStatus())
                .build();

    }

    @Test
    public void createSimCard(){
        when(cardRepository.save(any())).thenReturn(simCard);

        SimCardDto simCardDtoReturned =  simCardService.createSimCard(simCardDto);

        assertEquals(Optional.of(1L), Optional.ofNullable(simCardDtoReturned.getSimID()));
        assertEquals("ENGLAND",simCardDtoReturned.getCountryName());

    }

    @Test
    public void getAllSimCard() {

        List<SimCard> simCardList = List.of(simCard);

        when(cardRepository.findAll()).thenReturn(simCardList);

        List<SimCardDto> simCardDtoList = simCardService.getAllSimCard();

        assertEquals(1,simCardDtoList.size());
        assertEquals(Optional.of(1L), java.util.Optional.ofNullable(simCardDtoList.get(0).getSimID()));


    }

    @Test
    public void deleteSimCard(){
         when(cardRepository.existsById(any())).thenReturn(true);
         simCardService.deleteSimCard(1L);
         verify(cardRepository).deleteById(1L);
    }

    @Test(expected = SimCardNotFoundException.class)
    public void deleteSimCardWhenSimCardNotFound(){
        when(cardRepository.existsById(any())).thenReturn(false);
        simCardService.deleteSimCard(6L);
    }

    @Test
    public void updateSimCard(){
        simCardDto.setCountryName("BURSA");
        simCard.setCountryName("BURSA");

        when(cardRepository.existsById(any())).thenReturn(true);
        when(cardRepository.save(any())).thenReturn(simCard);

        SimCardDto simCardDtoReturned = simCardService.updateSimCard(simCardDto);
        assertEquals(Optional.of("BURSA"),Optional.ofNullable(simCardDtoReturned.getCountryName()));
    }
}
