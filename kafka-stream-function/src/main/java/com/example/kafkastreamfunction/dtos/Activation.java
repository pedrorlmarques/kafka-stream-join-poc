package com.example.kafkastreamfunction.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activation {

    private String activation;
    private Long createdDate;

}