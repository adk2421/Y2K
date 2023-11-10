package com.y2k.device;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class DeviceController {

    private final DeviceService deviceService;
    
    @GetMapping("dvcMng")
    public String dvcMngPage(Model model) {

        List<DeviceModel> dvcList = deviceService.getDvcList();
        
        for (DeviceModel device : dvcList) {
            System.out.println(device.getDvcId());
        }

        model.addAttribute("dvcList", dvcList);

        return "deviceMng";
    }

    @PostMapping("updateData")
    public void updateData(String dvcId) {

        System.out.println("####### 기기상태 : " + deviceService.getDvc("D-2023-01-B").getDvcStatus());
    }
}
