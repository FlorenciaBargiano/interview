package com.interview.bci.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericException extends Throwable {
    private LocalDateTime timeStamp;
    private int code;
    private String detail;
}
