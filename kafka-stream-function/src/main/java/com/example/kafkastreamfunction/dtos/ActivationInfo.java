package com.example.kafkastreamfunction.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationInfo {

    private String info;
    private Activation activation;

}