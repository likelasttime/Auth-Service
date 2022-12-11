package winterdevcamp.Auth.Service.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Salt;
import winterdevcamp.Auth.Service.model.UserRole;
import winterdevcamp.Auth.Service.repository.MemberRepository;

import java.util.UUID;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailService emailService;

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

    @Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/user/verify/";
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), member.getUsername(), 60 * 30L);
        emailService.sendMail(member.getEmail(), "Auth service 회원가입 인증 메일 입니다.", VERIFICATION_LINK + uuid.toString());
    }

    @Override
    public void verifyEmail(String key) throws NotFoundException{
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(memberId);
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        modifyUserRole(member, UserRole.ROLE_USER);
        redisUtil.deleteData(key);
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole){
        member.updateRole(userRole);
    }

    @Override
    public Member findByUsername(String username) throws NotFoundException{
        Member member = memberRepository.findByUsername(username);
        if(member == null) throw new NotFoundException("멤버가 조회되지 앟음");
        return member;
    }

}
