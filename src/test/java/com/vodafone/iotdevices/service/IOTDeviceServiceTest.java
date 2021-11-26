package com.vodafone.iotdevices.service;

import com.vodafone.iotdevices.dbmodel.IOTDevice;
import com.vodafone.iotdevices.dbmodel.SimCard;
import com.vodafone.iotdevices.dto.IOTDeviceDto;
import com.vodafone.iotdevices.dto.SimCardDto;
import com.vodafone.iotdevices.enums.SimCardStatusEnum;
import com.vodafone.iotdevices.exception.DeviceNotFoundException;
import com.vodafone.iotdevices.repository.IOTDeviceRepository;
import com.vodafone.iotdevices.repository.SimCardRepository;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IOTDeviceServiceTest {

    @InjectMocks
    @Spy
    private IOTDeviceService iotDeviceService;

    @Mock
    private IOTDeviceRepository deviceRepository;

    @Mock
    private SimCardRepository simCardRepository;

    @Mock
    private DozerBeanMapper dozerBeanMapper;

    @Mock
    private SimCardService simCardService;

    private IOTDevice iotDevice;
    private IOTDeviceDto iotDeviceDto;

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

        iotDevice = IOTDevice.builder()
                .id(2L)
                .temperature(50L)
                .simCard(simCard)
                .build();

        iotDeviceDto = IOTDeviceDto.builder()
                .id(iotDevice.getId())
                .isConfigurated(iotDevice.getIsConfigurated())
                .temperature(iotDevice.getTemperature())
                .simCard(simCardDto)
                .build();

//        when(dozerBeanMapper.map(any(SimCardDto.class), any())).thenReturn(simCard);
//        when(simCardRepository.save(any())).thenReturn(simCard);

//        when(dozerBeanMapper.map(any(IOTDeviceDto.class), any())).thenReturn(iotDevice);
//        when(deviceRepository.save(any())).thenReturn(iotDevice);

//        when(dozerBeanMapper.map(any(IOTDevice.class), any())).thenReturn(iotDeviceDto);

    }

    @Test
    public void  createDevice() {
        when(deviceRepository.save(any())).thenReturn(iotDevice);
        IOTDeviceDto iotDeviceDtoReturned = iotDeviceService.createDevice(iotDeviceDto);
        assertEquals(Optional.of(2L),Optional.ofNullable(iotDeviceDtoReturned.getId()));
        assertEquals(Optional.of(50L),Optional.ofNullable(iotDeviceDtoReturned.getTemperature()));
        assertEquals("ENGLAND",iotDeviceDtoReturned.getSimCard().getCountryName());
        assertEquals(SimCardStatusEnum.READY,iotDeviceDtoReturned.getSimCard().getSimCardStatus());

    }

    @Test
    public void getAllDevice(){
        List<IOTDevice> iotDeviceList =List.of(iotDevice);
        when(deviceRepository.findByOrderByIsConfiguratedDesc()).thenReturn(iotDeviceList);

        List<IOTDeviceDto> iotDeviceDtoList = iotDeviceService.getAllDevice();

        assertEquals(1,iotDeviceDtoList.size());
        assertEquals(Optional.of(2L), Optional.ofNullable(iotDeviceDtoList.get(0).getId()));
    }

    @Test
    public void deleteDevice(){
        when(deviceRepository.existsById(any())).thenReturn(true);
        iotDeviceService.deleteDevice(2L);
        verify(deviceRepository).deleteById(2l);
    }

    @Test(expected = DeviceNotFoundException.class)
    public void deleteDevice_whenDeviceNotFound(){

        when(deviceRepository.existsById(any())).thenReturn(false);
        iotDeviceService.deleteDevice(7L);
    }

    @Test
    public void updateDevice(){

        iotDeviceDto.setTemperature(60L);
        iotDevice.setTemperature(60L);

        when(deviceRepository.existsById(any())).thenReturn(true);
        when(deviceRepository.save(any())).thenReturn(iotDevice);

        IOTDeviceDto iotDeviceDtoReturned = iotDeviceService.updateDevice(iotDeviceDto);

        assertEquals(Optional.of(60L),Optional.ofNullable(iotDeviceDtoReturned.getTemperature()));
    }

    @Test
    public void getByStatus(){
        iotDeviceDto.setSimCard(null);
        simCardDto.setIotDevice(iotDeviceDto);
//        List<SimCard> simCardList = List.of(simCard);
        List<SimCardDto> simCardDtoList= List.of(simCardDto);

//        when(simCardRepository.findAllBySimCardStatus(any())).thenReturn(simCardList);
        when(simCardService.getByStatus(any())).thenReturn(simCardDtoList);
        List<IOTDeviceDto> iotDeviceDtoList = iotDeviceService.getByStatus(SimCardStatusEnum.READY);

        assertEquals(1,iotDeviceDtoList.size());
        assertEquals(Optional.of(50L), Optional.ofNullable(iotDeviceDtoList.get(0).getTemperature()));

    }
}