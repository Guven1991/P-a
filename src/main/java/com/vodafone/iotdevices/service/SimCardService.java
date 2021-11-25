package com.vodafone.iotdevices.service;

import com.vodafone.iotdevices.dbmodel.IOTDevice;
import com.vodafone.iotdevices.dbmodel.SimCard;
import com.vodafone.iotdevices.dto.IOTDeviceDto;
import com.vodafone.iotdevices.dto.SimCardDto;
import com.vodafone.iotdevices.enums.SimCardStatusEnum;
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


    public List<SimCardDto> getAllSimCard(){

        List<SimCard> simCardList = cardRepository.findAllBySimCardStatus(SimCardStatusEnum.READY);

        return  simCardList.stream().map(simCard ->
                dozerBeanMapper.map(simCard, SimCardDto.class)).collect(Collectors.toList());
    }

    public void deleteSimCard(Long id){

        if(!cardRepository.existsById(id)){
            throw  new SimCardNotFoundException("Simcard not Found");
        }
        cardRepository.deleteById(id);
    }

}
