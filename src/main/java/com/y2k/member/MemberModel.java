package com.y2k.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberModel {


    private String memberId;

    private String memberPw;

    private String memberPhone;

    private String createDate;

    public int getCount() {
        return 0;
    }

    public Object getMemberName() {
        return null;
    }

    public Object getMemberNo() {
        return null;
    }


    
}
