package com.zenpanda.crawler.exception.handler;

import com.zenpanda.crawler.exception.InvalidUrlException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CrawlerExceptionHandler {

    @ExceptionHandler(InvalidUrlException.class)
    ResponseEntity handle(InvalidUrlException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
