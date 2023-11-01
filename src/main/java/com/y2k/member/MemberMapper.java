package com.y2k.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    public int insert ( MemberModel memberModel );

    public int JoinMember ( MemberModel memberModel );

    public MemberModel Login ( MemberModel memberModel );
}
