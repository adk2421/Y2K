package com.y2k.test;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
    
    private final TestService testService;

    @GetMapping("select")
    public List<TestModel> select() {
        List<TestModel> result = testService.select();
        String str = "";

        for (TestModel item : result) 
            str += (item.getVal() + " ");

        System.out.println(str);

        return result;
    }
}
