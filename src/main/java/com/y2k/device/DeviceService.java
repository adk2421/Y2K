package com.y2k.device;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.invocation.CompletableFutureReturnValueHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Repository("deviceService")
@RequiredArgsConstructor
public class DeviceService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    private String path = "com.y2k.device.DeviceMapper.";

    public List<DeviceModel> getDvcList() {
        return sqlSessionTemplate.selectList(path + "getDvcList");
    }

    @Async
    public CompletableFuture<DeviceModel> getDvc(String dvcId) {
        return CompletableFuture.completedFuture(sqlSessionTemplate.selectOne(path + "getDvc", dvcId));
    }

    public void addDvc(DeviceModel deviceModel) {
        sqlSessionTemplate.insert(path + "addDvc", deviceModel);
    }

    @Transactional
    public void modifyDvc(DeviceModel deviceModel) {
        sqlSessionTemplate.update(path + "modifyDvc", deviceModel);
    }

    @Async
    public void removeDvc(String dvcIdList) throws NumberFormatException, InterruptedException {
        Thread.sleep(15000-(long)Math.random()*10000);
        sqlSessionTemplate.update(path + "removeDvc", dvcIdList);
    }

    @Async
    public void updateDvcStts(String dvcId) {
        sqlSessionTemplate.update(path + "updateDvcStts", dvcId);
    }

    @Async
    public void updateAllDvcUpdtStts() {
        sqlSessionTemplate.update(path + "updateAllDvcUpdtStts");
    }

    @Async
    public void updateDvcUpdtStts(String dvcId) {
        sqlSessionTemplate.update(path + "updateDvcUpdtStts", dvcId);
    }

    @Async
    public CompletableFuture<Integer> getAllDvcOpStts() {
        return CompletableFuture.completedFuture(sqlSessionTemplate.selectOne(path + "getAllDvcOpStts"));
    }

    @Async
    public void updateDvcOpStts(DeviceModel deviceModel) {
        sqlSessionTemplate.update(path + "updateDvcOpStts", deviceModel);
    }

    @Async("sampleExecutor")
    public void fileTrans(List<MultipartFile> files, String filePath) throws IllegalStateException, IOException {
        File fileName;

        for ( MultipartFile file : files ) {
            fileName = new File(filePath + "\\" + file.getOriginalFilename());
            System.out.println(fileName);
            file.transferTo(fileName);
        }

        LOG.info("####################### " + files.get(0).getOriginalFilename());
    }
}
