package com.y2k.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository("asyncService")
@RequiredArgsConstructor
public class AsyncService {

    @Async
    public void asyncTest(int num) {
        try {
            System.out.println("exexute : " + num);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Async
    public void getUpadteStts(int data) {
        try {
            System.out.println("테스트 버튼 : " + data);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }

    @Async
    public void asyncFileTransfer(int num) {
        System.out.println("");
    }
}
