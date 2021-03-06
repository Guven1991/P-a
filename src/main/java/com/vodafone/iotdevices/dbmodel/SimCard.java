package com.vodafone.iotdevices.dbmodel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vodafone.iotdevices.enums.SimCardStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "pia",name= "sim_card")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimCard {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="sim_card_seq")
    @SequenceGenerator(schema = "pia", sequenceName = "sim_card_seq", allocationSize = 1, name = "sim_card_seq")
    @Column(name="sim_id",nullable = false)
    private Long simID;

    @Column(name = "operator_code")
    private Long operatorCode;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SimCardStatusEnum simCardStatus;

    @OneToOne(mappedBy = "simCard",fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JsonBackReference
    private IOTDevice iotDevice;
}