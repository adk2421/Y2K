package com.y2k.async;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncService asyncService;

    @GetMapping("asyncTest")
    public String asyncTest() {
        for (int i = 0; i < 15; i++) {
            asyncService.asyncTest(i);
        }

        return "test 성공";
    }

    @Async
    @GetMapping("liveSign")
    public void liveSign(@RequestParam int data) {
        asyncService.getUpadteStts(data);
    }
    
    @Async
    @ResponseBody
    @GetMapping("asyncFileTransfer")
    public CompletableFuture<ResponseEntity<Resource>> asyncFileTransfer() throws IOException {
        String line;
        File file;

        Process proc = Runtime.getRuntime().exec("cmd /c dir d:\\testFile /a:a /s /b");
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        while (null != (line = br.readLine())) {
            file = new File(line);
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getParentFile().getPath());
            System.out.println(file.getName());
        }

        return null;
    }
}
