package winterdevcamp.Auth.Service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @Column(unique = true)
    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salt_id")
    private Salt salt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;

    @Builder
    public Member(String username, String password, String name, String email, UserRole role, Salt salt){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = UserRole.ROLE_NOT_PERMITTED;
        this.salt = salt;
    }

    public void updateRole(UserRole role){
        this.role = role;
    }

    public void updateSaltAndPassword(Salt salt, String password){
        this.salt = salt;
        this.password = password;
    }
}
