package com.interview.bci.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends Throwable{
    private LocalDateTime timeStamp;
    private int code;
    private String detail;
}
