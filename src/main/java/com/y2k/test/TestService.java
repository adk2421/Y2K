package com.y2k.test;

import java.util.List;
import java.io.IOException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.y2k.member.MemberModel;

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

    public ResponseEntity<Resource> fileDownload() {
        return null;
    }

    public MultiValueMap<String, Object> getMultivalueMap(List<MultipartFile> files) throws IOException {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        for(MultipartFile file : files) {
            ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()){
                @Override
                public String getFilename(){
                    return file.getOriginalFilename();
                }
            };
            map.add("files", contentsAsResource);
        }
        return map;
    }
}
