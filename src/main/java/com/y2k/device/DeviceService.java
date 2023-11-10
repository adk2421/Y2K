package com.y2k.device;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@EnableAsync
@Repository("deviceService")
@RequiredArgsConstructor
public class DeviceService {
    
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    private String path = "com.y2k.device.DeviceMapper.";

    public List<DeviceModel> getDvcList() {
        return sqlSessionTemplate.selectList(path + "getDvcList");
    }

    public DeviceModel getDvc(String dvcId) {
        return sqlSessionTemplate.selectOne(path + "getDvc", dvcId);
    }
}
