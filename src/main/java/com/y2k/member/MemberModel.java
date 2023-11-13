package com.y2k.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name="Member", description = "회원")
public class MemberModel {


    @Schema(name = "memberId", description = "아이디", example = "kim")
    private String memberId;

    @Schema(name = "memberPw", description = "비밀번호")
    private String memberPw;

    @Schema(name = "memberName", description = "회원명", example = "홍길동길동")
    private String memberName;

    @Schema(name = "memberPhone", description = "전화번호", example = "010-1111-2222")
    private String memberPhone;

    @Schema(name = "createDate", description = "등록일", example = "2023-11-01")
    private String createDate;

    @Schema(name = "certToken", description = "인증토큰", example = "eyJ0eXAiOiJ...")
    private String certToken;

    public int getCount() {
        return 0;
    }

    public Object getMemberNo() {
        return null;
    }
    
}
