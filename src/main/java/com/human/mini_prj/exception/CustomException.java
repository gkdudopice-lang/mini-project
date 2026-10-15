package com.human.mini_prj.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String code;

    public CustomException(String code, String message) {
        super(message); // 부모 클래스(RuntimeException)의 생성자에 메시지를 전달해야 getMessage()를 쓸 수 있습니다!
        this.code = code;
    }
}