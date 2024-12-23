import io.litmuschaos.LitmusClient;
import io.litmuschaos.exception.LitmusApiException;
import io.litmuschaos.exception.detailed.UnauthorizedException;
import io.litmuschaos.request.LoginRequest;
import io.litmuschaos.response.CapabilityResponse;
import io.litmuschaos.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class AuthTest {

    private static final String hostUrl = "http://localhost:3000";
    private static final String username = "admin";
    private static final String password = "Litmus1234!";

    private LitmusClient authClient;

    @BeforeEach
    public void setup() throws IOException, LitmusApiException {
        this.authClient = new LitmusClient(hostUrl, username, password);
    }

    @Test
    public void testCapabilityAPI() throws IOException, LitmusApiException {

        assertThat(authClient.capabilities())
                .isNotNull()
                .isInstanceOf(CapabilityResponse.class)
                .hasFieldOrProperty("Dex");
    }

    @Test
    public void testAuthenticationAPI() throws IOException, LitmusApiException {
        LoginRequest request = LoginRequest.builder().username(username).password(password).build();
        assertThat(authClient.authenticate(request))
                .isNotNull()
                .isInstanceOf(LoginResponse.class);
    }

    @Test
    public void testAuthenticationAPIFail() {
        // Given
        String wrongPassword = "litmus1234";
        LoginRequest request = LoginRequest.builder().username(username).password(wrongPassword).build();

        // When & Then
        assertThatThrownBy(() -> authClient.authenticate(request))
                .isInstanceOf(UnauthorizedException.class);
    }
}
