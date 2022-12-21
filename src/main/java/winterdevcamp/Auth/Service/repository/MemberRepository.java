package winterdevcamp.Auth.Service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.UserRole;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

    List<Member> findAll();

    Page<Member> findAllByRoleNotLike(UserRole role, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteByUsername(String username);
}
