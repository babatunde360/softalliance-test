package com.babatunde.employee.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseBody<T> {
    private boolean success;
    private String message;
    private T data;



    public ResponseBody(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}