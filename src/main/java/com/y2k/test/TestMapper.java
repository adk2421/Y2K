package com.y2k.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
    
    List<TestModel> select();

}
