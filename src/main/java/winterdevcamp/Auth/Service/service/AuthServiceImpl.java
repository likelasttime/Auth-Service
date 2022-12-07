package winterdevcamp.Auth.Service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Salt;
import winterdevcamp.Auth.Service.repository.MemberRepository;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SaltUtil saltUtil;

    @Override
    public void signUpMember(Member member){
        String password = member.getPassword();
        String salt = saltUtil.genSalt();
        Member member1 = Member.builder()
                .username(member.getUsername())
                .name(member.getName())
                .email(member.getEmail())
                .salt(Salt.builder().salt(salt).build())
                .password(saltUtil.encodePassword(salt, password))
                .build();

        memberRepository.save(member1);
    }

    @Override
    public Member loginMember(String id, String password) throws Exception{
        Member member = memberRepository.findByUsername(id);
        if(member == null) throw new Exception ("멤버가 조회되지 않음");
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        if(!member.getPassword().equals(password)) throw new Exception ("비밀번호가 다릅니다.");

        return member;
    }

}
