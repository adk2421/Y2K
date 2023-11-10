package com.y2k.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.y2k.member.MemberModel;

@Mapper
public interface TestMapper {
    
    List<TestModel> select();

    MemberModel selectMember(MemberModel memberModel);

    void insertToken(MemberModel memberModel);

    String getToken(String token);

}
