package com.y2k.device;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper {
    
    public List<DeviceModel> getDvcList();

    public DeviceModel getDvc(String dvcId);

    public void addDvc(DeviceModel deviceModel);

    public void modifyDvc(DeviceModel deviceModel);

    public void removeDvc(String dvcIdList);

    public void updateDvcStts(String dvcId);

    public void updateAllDvcUpdtStts();

    public void updateDvcUpdtStts(String dvcId);

    public int getAllDvcOpStts();

    public void updateDvcOpStts(DeviceModel deviceModel);
}
