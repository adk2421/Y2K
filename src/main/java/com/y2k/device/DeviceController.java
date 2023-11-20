package com.y2k.device;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class DeviceController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final String logPrefix = "########################### ";

    // application.yml에 작성한 파일 경로 전역변수 처리 후 사용하기 위해
    @Value("${spring.servlet.multipart.location}")
    private String filePath;
    
    private final DeviceService deviceService;
    
    @GetMapping("dvcMng")
    public String dvcMngPage(Model model) {
        model.addAttribute("dvcList", deviceService.getDvcList());
        return "deviceMng";
    }

    @GetMapping("dvcDetail")
    public String dvcDetailPage(@RequestParam String dvcId, Model model) throws InterruptedException, ExecutionException {
        System.out.println(dvcId);
        DeviceModel dModel = deviceService.getDvc(dvcId).get();
        System.out.println(dModel.getDvcLastAcsTime());
        return "deviceDetail";
    }

    @Async
    @ResponseBody
    @PostMapping("addDvc")
    public CompletableFuture<Integer> addDvc(DeviceModel deviceModel) {
        deviceService.addDvc(deviceModel);
        return CompletableFuture.completedFuture(200);
    }

    @Async
    @ResponseBody
    @PutMapping("modifyDvc")
    public CompletableFuture<Integer> modifyDvc(DeviceModel deviceModel) {
        deviceService.modifyDvc(deviceModel);
        return CompletableFuture.completedFuture(200);
    }

    @Async
    @ResponseBody
    @PutMapping("removeDvc")
    public CompletableFuture<Integer> removeDvc(String[] dvcIdArr) throws NumberFormatException, InterruptedException {
        // String dvcIdList = String.join("', '", dvcIdArr);
        // deviceService.removeDvc(dvcIdList);
        for (String dvcId : dvcIdArr) {
            deviceService.removeDvc(dvcId);
        }
        return CompletableFuture.completedFuture(200);
    }

    @Async
    @ResponseBody
    @PutMapping("checkUpdate")
    public CompletableFuture<Integer> checkUpdate(String dvcId) throws InterruptedException, ExecutionException {
        String resMsg = "이미 최신 상태입니다.";

        // 기기 정보 조회
        DeviceModel deviceModel = deviceService.getDvc(dvcId).get();

        // 기기 업데이트 상태 조회 - 업데이트가 필요하면 진입
        if (deviceModel.getDvcUpdtStts() == 1) {
            LOG.info(logPrefix + "업데이트 필요", dvcId);

            if (deviceService.getAllDvcOpStts().get() == 0) {
                LOG.info(logPrefix + "다운로드 중인 기기 없음");

                // 다운로드 중으로 상태 변경
                deviceModel.setDvcOpStts(3);
                deviceService.updateDvcOpStts(deviceModel);

                // 다운로드 로직
                Thread.sleep(10000);

                // Live(1), 배포유무(0) 업데이트
                deviceService.updateDvcStts(dvcId);
                deviceService.updateDvcUpdtStts(dvcId);

                // 다운로드 완료로 상태 변경
                deviceModel.setDvcOpStts(9);
                deviceService.updateDvcOpStts(deviceModel);

                resMsg = "업데이트가 완료되었습니다.";

            } else {
                LOG.info(logPrefix + "다운로드 중인 기기 있음");

                // 다운로드 대기중으로 상태 변경
                deviceModel.setDvcOpStts(2);
                deviceService.updateDvcOpStts(deviceModel);

                resMsg = "다운로드 진행 중인 기기가 있습니다.";
            }
        }

        return CompletableFuture.completedFuture(200);
    }

    @ResponseBody
    @PutMapping("fileUpload")
    public int fileUpload() {
        deviceService.updateAllDvcUpdtStts();
        return 200;
    }

    // @ResponseBody
    // @PostMapping("dvcFileTrans")
    // public String dvcFileTrans(@RequestBody List<MultipartFile> files) throws IOException {
    //     String directoryPath = "D:\\dvcFile";

    //     System.out.println("********inside client  *****************");
    //     System.out.println("**** Number of files : " + files.size());

    //     // parent directory를 찾는다.
    //     Path directory = Paths.get(directoryPath).toAbsolutePath().normalize();

    //     // directory 해당 경로까지 디렉토리를 모두 만든다.
    //     Files.createDirectories(directory);

    //     for (MultipartFile file : files) {
    //         // 파일명을 바르게 수정한다.
    //         String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    //         // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
    //         Assert.state(!fileName.contains(".."), "Name of file cannot contain '..'");
    //         // 파일을 저장할 경로를 Path 객체로 받는다.
    //         Path targetPath = directory.resolve(fileName).normalize();

    //         // 파일이 이미 존재하는지 확인하여 존재한다면 오류를 발생하고 없다면 저장한다.
    //         Assert.state(!Files.exists(targetPath), fileName + " File alerdy exists.");
    //         file.transferTo(targetPath);
    //     }

    //     return "200";
    // }


    /**
     * 파일 전송
     * @param files
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("dvcFileTrans")
    public void dvcFileTrans(@RequestBody List<MultipartFile> files) throws IOException {
        deviceService.fileTrans(files, filePath);
    }

    /**
     * 파일 압축
     * @param fileName
     * @param fileBytes
     * @return
     * @throws IOException
     */
    private byte[] compressFile(String fileName, byte[] fileBytes) throws IOException {
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream();
              ZipOutputStream zipOutputStream = new ZipOutputStream(baos); ) {

            // 압축 엔트리 생성
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);

            // 파일을 압축 스트림에 쓰기
            zipOutputStream.write(fileBytes);

            // 압축 스트림과 출력 스트림 닫기
            zipOutputStream.closeEntry();

            return baos.toByteArray();
        }
    }
}
