package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//spring이 가져있는 Transactional
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     *
     회원가입
     */
    @Transactional
    public Long join(Member member){
        validationDuplicateMember(member); // 중복회원검
        memberRepository.save(member);
        return member.getId();
    }

    private void validationDuplicateMember(Member member) {
        //DB name unique 제약조건 넣어야함
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
