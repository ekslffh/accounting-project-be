package com.example.hsap.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageInfoTest {

    @Test
    public void randomWordTest() {
        String newWord = MessageInfo.getNewWord(10);
        System.out.println(newWord);
    }

}