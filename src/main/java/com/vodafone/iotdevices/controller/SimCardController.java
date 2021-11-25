package com.vodafone.iotdevices.controller;

import com.vodafone.iotdevices.dto.SimCardDto;
import com.vodafone.iotdevices.response.IOTDeviceResponse;
import com.vodafone.iotdevices.response.SimCardResponse;
import com.vodafone.iotdevices.service.SimCardService;
import org.dozer.DozerBeanMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/simCard")
public class SimCardController {

    private final SimCardService simCardService;
    DozerBeanMapper dozerMapper = new DozerBeanMapper();

    public SimCardController(SimCardService simCardService) {
        this.simCardService = simCardService;
    }


    @GetMapping
    public ResponseEntity<List<SimCardResponse>> getAllSimCard() {

        List<SimCardDto> simCardDtoList = simCardService.getAllSimCard();
        List<SimCardResponse> simCardResponseList = simCardDtoList.stream().map(simCard ->
                        dozerMapper.map(simCard, SimCardResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(simCardResponseList);
    }

    @DeleteMapping("/{id}")
    public void deleteSimCard(@PathVariable("id") Long id) {

        simCardService.deleteSimCard(id);
    }
}
