package com.y2k.device;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceModel {

    // 기기 ID
    private String dvcId;

    // 기기 유형
    private String dvcType;

    // 기기 이름
    private String dvcName;

    // 기기 사용 지점
    private String dvcBrc;

    // 기기 상태
    private String dvcStatus;

    // 기기 사용 유무
    private String dvcUseYn;

    // 기기 마지막 접근 시간
    private String dvcLastAcsTime;
    
}
