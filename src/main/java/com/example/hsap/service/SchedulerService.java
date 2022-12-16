//package com.example.hsap.service;
//
//import net.nurigo.sdk.NurigoApp;
//import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
//import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
//import net.nurigo.sdk.message.exception.NurigoUnknownException;
//import net.nurigo.sdk.message.model.Message;
//import net.nurigo.sdk.message.service.DefaultMessageService;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SchedulerService {
//
//    private final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCS3CDJQNKS6YNXZ", "AO8BIRTEPZLKY2CMRBZ8VCY0RDPT4ASM", "https://api.solapi.com");
//
//    @Scheduled(cron = "0 40 14 * * *")
//    public void run() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
//        Message message = new Message();
//        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
//        message.setFrom("01084819926");
//        message.setTo("01084819926");
//        message.setText("test 중");
//
//        this.messageService.send(message);
//    }
//
//}
