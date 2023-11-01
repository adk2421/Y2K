package com.y2k.member;

public interface MemberService {

    public int insert ( MemberModel memberModel ) throws Exception ;

    public int JoinMember ( MemberModel memberModel ) throws Exception ;

    public MemberModel Login ( MemberModel memberModel ) throws Exception ;

}
