package winterdevcamp.Auth.Service.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestVerifyEmail {
    String username;
}
