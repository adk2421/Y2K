package com.y2k.device;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper {
    
    public List<DeviceModel> getDvcList();

    public DeviceModel getDvc(String dvcId);
}
