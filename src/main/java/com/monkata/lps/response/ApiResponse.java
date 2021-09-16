package com.monkata.lps.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ApiResponse{
    @JsonProperty("ERROR") 
    public boolean error;
    @JsonProperty("DATA") 
    public Order data;
}


	


