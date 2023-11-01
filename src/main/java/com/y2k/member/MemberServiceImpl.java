package com.y2k.member;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private String path = "com.y2k.member.MemberMapper.";

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private final MemberMapper memberMapper;

        @Override
        public int insert(MemberModel memberModel) {
            return sqlSessionTemplate.insert(path + "insert", memberModel);
        }

        // 회원가입
        @Override
        public int JoinMember(MemberModel memberModel) throws Exception {
            // log.info(" _ JoinMember _ ");
            return memberMapper.JoinMember(memberModel);
        }

        // 로그인
        @Override
        public MemberModel Login(MemberModel memberModel) throws Exception {
            log.info(" _ Login _ ");
        return memberMapper.Login(memberModel);
    }

}
        