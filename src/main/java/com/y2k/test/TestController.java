package com.y2k.test;

import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.y2k.jsch.JschWrapper;
import com.y2k.jwt.JwtTokenProvider;
import com.y2k.member.MemberModel;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
    
    private final TestService testService;

    private final JwtTokenProvider jwtTokenProvider;

    // @Value("${servlet.multipart.location}")
    private String filePath = "c:\\Temp\\upload";
    
    @GetMapping("select")
    public List<TestModel> select() {
        List<TestModel> result = testService.select();
        String str = "";

        for (TestModel item : result) 
            str += (item.getVal() + " ");

        System.out.println(str);

        return result;
    }

    /**
     * 토큰 생성
     * @param response
     * @param member
     * @return
     */
    @PutMapping("initializeToken")
    public int initializeToken(HttpServletResponse response, MemberModel member) {
        int resultCode = 0;
        
        member = new MemberModel();
        member.setMemberId("adk112");
        member.setMemberPw("123");

        resultCode = testService.selectMember(member);

        if (testService.selectMember(member) == 200) {
            final String token = jwtTokenProvider.createToken(member);
            member.setCertToken(token);

            testService.insertToken(member);

            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return resultCode;
    }

    /**
     * 토큰 인증
     * @param request
     * @return
     */
    @GetMapping("verifyToken")
    public Claims verifyToken(HttpServletRequest request) {
        String token = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwtToken".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }

        System.out.println(token);

        return jwtTokenProvider.decodeToken(token);
    }

    @GetMapping("syncTest")
    public List<String> syncTest() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            list.add(testService.syncTest(i));
        }

        return list;
    }

    @GetMapping("asyncTest")
    public String asyncTest() {
        for (int i = 0; i < 15; i++) {
            testService.asyncTest(i);
        }

        return "test 성공";
    }


    /**
     * SFTP 파일업로드 1
     * @return
     */
    @GetMapping("uploadFile")
    public String uploadFile() {
        JschWrapper jschWrapper = null;
        String url = "220.121.40.164";

        try {
            jschWrapper = new JschWrapper();

            // SFTP 접속하기 (주소, 포트번호, 사용자아이디, 패스워드)
            jschWrapper.connectSFTP(url, 22, "user", "1234");

            // /uploadFolder 폴더 위치에 testFile 폴더 생성
            jschWrapper.mkdir("/uploadFolder", "testFile");

            // C:\\Users\\user\\Pictures\\omong.jpg 파일을 /uploadFolder/testFile 위치에 업로드
            jschWrapper.uploadFile("C:\\Users\\user\\Pictures\\omong.jpg", "/uploadFolder/testFile");

            // /drive/u/0/home/file/omong.jpg 파일을 D:\fileUpload 위치에 다운로드
            jschWrapper.downloadFile("/uploadFolder/testFile/omong.jpg", "D:\\fileUpload", false);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // SFTP 접속해제
            jschWrapper.disconnectSFTP();
        }

        return "업로드 성공";
    }

    
    /**
     * SFTP 파일업로드 2
     * @return
     */
    @GetMapping("uploadFile2")
    public String uploadFile2() {
        String host = "220.121.40.164";
        String username = "user";
        String password = "1234";
        int port = 22; // Custom SFTP port

        String localFilePath = "C:\\Users\\user\\Pictures\\omong.jpg";
        String remoteFilePath = "/uploadFolder";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // Disable host key checking

            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.put(localFilePath, remoteFilePath);

            channelSftp.disconnect();
            session.disconnect();
            
            System.out.println("File uploaded successfully");

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }

        return "업로드2 성공";
    }

    @GetMapping("fileDownload")
    public ResponseEntity<Resource> fileDownload() throws MalformedURLException {
        Resource resource = new UrlResource(Paths.get(filePath + "\\" + "omong.jpg").toUri());

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "omong.jpg")
            .body(resource);
    }
}
