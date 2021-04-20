package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenberService {
    @Autowired
    private MemberRepository memberRepository;

    /**
     *
     회원가입
     */
    public Long join(Member member){
        validationDuplicateMember(member); // 중복회원검
        memberRepository.save(member);
        return member.getId();
    }

    private void validationDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    //회원 전체조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 한건 조회

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
