package com.vodafone.iotdevices.service;

import com.vodafone.iotdevices.dbmodel.IOTDevice;
import com.vodafone.iotdevices.dbmodel.SimCard;
import com.vodafone.iotdevices.dto.IOTDeviceDto;
import com.vodafone.iotdevices.dto.SimCardDto;
import com.vodafone.iotdevices.enums.SimCardStatusEnum;
import com.vodafone.iotdevices.exception.DeviceNotFoundException;
import com.vodafone.iotdevices.exception.SimCardNotFoundException;
import com.vodafone.iotdevices.repository.IOTDeviceRepository;
import com.vodafone.iotdevices.repository.SimCardRepository;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimCardService {

    private final IOTDeviceRepository deviceRepository;

    private final SimCardRepository cardRepository;

    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();


    public SimCardDto createSimCard(SimCardDto simCardDto) {

        SimCard simCard = cardRepository.save(dozerBeanMapper.map(simCardDto, SimCard.class));
        return dozerBeanMapper.map(cardRepository.save(simCard), SimCardDto.class);
    }

    public List<SimCardDto> getAllSimCard() {

        List<SimCard> simCardList = cardRepository.findAll();

        return simCardList.stream().map(simCard ->
                dozerBeanMapper.map(simCard, SimCardDto.class)).collect(Collectors.toList());
    }

    public void deleteSimCard(Long id) {

        if (!cardRepository.existsById(id)) {
            throw new SimCardNotFoundException("Simcard not Found");
        }
        cardRepository.deleteById(id);
    }

    public SimCardDto updateSimCard(SimCardDto simCardDto) {

        if (!cardRepository.existsById(simCardDto.getSimID())) {
            throw new SimCardNotFoundException("SimCard not found!");
        }
        SimCard simCard = cardRepository.save(dozerBeanMapper.map(simCardDto, SimCard.class));
        return dozerBeanMapper.map(simCard,SimCardDto.class);
    }

    public List<SimCardDto> getByStatus(SimCardStatusEnum simCardStatusEnum){

        List<SimCard> simCardStatusList = cardRepository.findAllBySimCardStatus(simCardStatusEnum);

        return  simCardStatusList.stream().map(simCard ->
                dozerBeanMapper.map(simCard,SimCardDto.class)).collect(Collectors.toList());
    }

}
