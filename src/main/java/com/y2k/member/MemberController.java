package com.y2k.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Tag(name = "회원가입 API", description = "Swagger 테스트용 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {
    

    @Autowired
    private final MemberService memberService;

    /**
     * 회원가입 기능
     *
     * Request Param Type
     * {
     * "memberId" : "",
     * "memberPw" : "",
     * "memberName" : "",
     * "memberPhone" : ""
     * }
     *
     * return -> FAIL ( 오류 ) / 성공 시 SUCC
     */
    @Operation(summary = "회원가입", description = "회원DB에 회원 객체를 INSERT 합니다.")
    @Parameter(name = "memberModel", description = "회원 객체")
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    @ResponseBody
    public String JoinMember(@RequestBody MemberModel memberModel) throws Exception {
        System.out.println("join 진입");
        int result = memberService.insert(memberModel);
        // log.info("info = " + memberModel);
        // try {
        //     memberService.JoinMember(memberModel);
        //     return "SUCC";
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return "FAIL";
        // }
        return "SUCC";
    }


        /**
     * 로그인 기능
     *
     * Request Param Type
     * {
     * "userEmail" : "",
     * "userPw" : ""
     * }
     *
     * return -> FAIL ( 오류 ) / 성공 시 SUCC , 세션에 데이터 저장
     */
    @Hidden
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String loginMember(@RequestBody MemberModel memberModel, HttpSession session) throws Exception {

        // 옵셔널 사용 , String.valueOf( ) ==> null check
        MemberModel member = memberService.Login(memberModel);

        if (member.getCount() > 0) {
            session.setAttribute("memberName", member.getMemberName());
            session.setAttribute("memberNo", member.getMemberNo());
            return "SUCC";
        } else {
            return "FAIL";
        }

    }

    @Hidden
    @RequestMapping("test")
    @ResponseBody
    public String test() throws Exception {
        return "SUCC ";
    }
    
}
