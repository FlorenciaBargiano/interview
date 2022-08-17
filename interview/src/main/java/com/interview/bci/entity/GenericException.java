package com.interview.bci.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericException extends Throwable {
    private LocalDateTime timeStamp;
    private int code;
    private String detail;
}
