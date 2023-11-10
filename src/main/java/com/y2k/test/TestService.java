package com.y2k.test;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import com.y2k.member.MemberModel;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@EnableAsync
@Repository("testService")
@RequiredArgsConstructor
public class TestService {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    private String path = "com.y2k.test.TestMapper.";

    public List<TestModel> select() {
        return sqlSessionTemplate.selectList(path + "select");
    }

    public int selectMember(MemberModel memberModel) {
        int resultCode = 0;
        MemberModel member = sqlSessionTemplate.selectOne(path + "selectMember", memberModel);

        if (member != null) {
            resultCode = 200;
        }

        return resultCode;
    }

    public void insertToken(MemberModel memberModel) {
        sqlSessionTemplate.update(path + "insertToken", memberModel);
    }

    public String getToken(String token) {
        return sqlSessionTemplate.selectOne(path + "getToken", token);
    }

    public String syncTest(int num) {
        return "exexute : " + num;
    }

    @Async
    public void asyncTest(int num) {
        System.out.println("exexute : " + num);
    }

    public ResponseEntity<Resource> fileDownload() {
        return null;
    }
}
