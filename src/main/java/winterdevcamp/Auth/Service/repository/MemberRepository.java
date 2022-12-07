package winterdevcamp.Auth.Service.repository;

import org.springframework.data.repository.CrudRepository;
import winterdevcamp.Auth.Service.model.Member;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

    List<Member> findAll();

}
