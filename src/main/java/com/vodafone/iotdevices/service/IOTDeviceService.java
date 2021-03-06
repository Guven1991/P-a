package com.vodafone.iotdevices.service;

import com.vodafone.iotdevices.dbmodel.IOTDevice;
import com.vodafone.iotdevices.dbmodel.SimCard;
import com.vodafone.iotdevices.dto.IOTDeviceDto;
import com.vodafone.iotdevices.dto.SimCardDto;
import com.vodafone.iotdevices.enums.SimCardStatusEnum;
import com.vodafone.iotdevices.exception.DeviceNotFoundException;
import com.vodafone.iotdevices.repository.IOTDeviceRepository;
import com.vodafone.iotdevices.repository.SimCardRepository;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IOTDeviceService {

    private final IOTDeviceRepository deviceRepository;


    private final SimCardService simCardService;
    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    public IOTDeviceDto createDevice(IOTDeviceDto iotDeviceDto) {

        iotDeviceDto.setIsConfigurated(isConfiguratedDevice(iotDeviceDto));

        SimCardDto simCardDto = simCardService.createSimCard(iotDeviceDto.getSimCard());

        SimCard simCard = dozerBeanMapper.map(simCardDto, SimCard.class);

        IOTDevice iotDevice = dozerBeanMapper.map(iotDeviceDto, IOTDevice.class);

        iotDevice.setSimCard(simCard);
        IOTDevice iotDevice1 = deviceRepository.save(iotDevice);

        return dozerBeanMapper.map(iotDevice1, IOTDeviceDto.class);
    }


    public List<IOTDeviceDto> getAllDevice() {

        List<IOTDevice> iotDeviceList = deviceRepository.findByOrderByIsConfiguratedDesc();

        return iotDeviceList.stream().map(device ->
                        dozerBeanMapper.map(device, IOTDeviceDto.class))
                .collect(Collectors.toList());
    }

    public void deleteDevice(Long id) {

        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException("Device not found!");
        }
        deviceRepository.deleteById(id);
    }

    public IOTDeviceDto updateDevice(IOTDeviceDto iotDeviceDto) {

        if (!deviceRepository.existsById(iotDeviceDto.getId())) {
            throw new DeviceNotFoundException("Device not found!");
        }

        iotDeviceDto.setIsConfigurated(isConfiguratedDevice(iotDeviceDto));
        IOTDevice iotDevice = deviceRepository.save(dozerBeanMapper.map(iotDeviceDto, IOTDevice.class));
        return dozerBeanMapper.map(iotDevice, IOTDeviceDto.class);
    }

    public List<IOTDeviceDto> getByStatus(SimCardStatusEnum simCardStatusEnum) {

        List<SimCardDto> simCardDtoList = simCardService.getByStatus(simCardStatusEnum);

        return simCardDtoList.stream().map(SimCardDto::getIotDevice).collect(Collectors.toList());
    }

    public boolean isConfiguratedDevice(IOTDeviceDto iotDeviceDto) {
        return iotDeviceDto.getSimCard().getSimCardStatus().equals(SimCardStatusEnum.READY) &&
                (iotDeviceDto.getTemperature() > -25 && iotDeviceDto.getTemperature() < 85);
    }
}
