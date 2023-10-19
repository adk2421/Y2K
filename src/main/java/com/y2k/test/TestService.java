package com.y2k.test;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Repository("testService")
@RequiredArgsConstructor
public class TestService {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    private String path = "com.y2k.test.TestMapper.";

    public List<TestModel> select() {
        return sqlSessionTemplate.selectList(path + "select");
    }
}
