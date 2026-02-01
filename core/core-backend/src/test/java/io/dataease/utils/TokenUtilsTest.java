package io.dataease.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.dataease.auth.bo.TokenUserBO;
import io.dataease.exception.DEException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Tag("TokenUtils")
class TokenUtilsTest {

    private static String createValidToken(long uid, long oid) {
        return JWT.create()
                .withClaim("uid", uid)
                .withClaim("oid", oid)
                .sign(Algorithm.HMAC256("test-secret"));
    }

    @Test
    void validate_blankToken_throwsWithUri() {
        try (MockedStatic<ServletUtils> servletUtils = mockStatic(ServletUtils.class)) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/api/dataset/list");
            servletUtils.when(ServletUtils::request).thenReturn(request);

            assertThatThrownBy(() -> TokenUtils.validate(""))
                    .isInstanceOf(DEException.class)
                    .hasMessageContaining("token is empty")
                    .hasMessageContaining("/api/dataset/list");
        }
    }

    @Test
    void validate_nullToken_throwsWithUri() {
        try (MockedStatic<ServletUtils> servletUtils = mockStatic(ServletUtils.class)) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/api/test");
            servletUtils.when(ServletUtils::request).thenReturn(request);

            assertThatThrownBy(() -> TokenUtils.validate(null))
                    .isInstanceOf(DEException.class)
                    .hasMessageContaining("token is empty");
        }
    }

    @Test
    void validate_shortToken_throwsInvalid() {
        assertThatThrownBy(() -> TokenUtils.validate("short"))
                .isInstanceOf(DEException.class)
                .hasMessageContaining("token is invalid");
    }

    @Test
    void validate_validToken_returnsTokenUserBO() {
        String token = createValidToken(123L, 456L);

        TokenUserBO result = TokenUtils.validate(token);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(123L);
        assertThat(result.getDefaultOid()).isEqualTo(456L);
    }

    @Test
    void userBOByToken_validToken_returnsTokenUserBO() {
        String token = createValidToken(999L, 1L);

        TokenUserBO result = TokenUtils.userBOByToken(token);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(999L);
        assertThat(result.getDefaultOid()).isEqualTo(1L);
    }

    @Test
    void validateLinkToken_blankToken_throwsWithUri() {
        try (MockedStatic<ServletUtils> servletUtils = mockStatic(ServletUtils.class)) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/share/link");
            servletUtils.when(ServletUtils::request).thenReturn(request);

            assertThatThrownBy(() -> TokenUtils.validateLinkToken(""))
                    .isInstanceOf(DEException.class)
                    .hasMessageContaining("link token is empty");
        }
    }

    @Test
    void validateLinkToken_shortToken_throwsInvalid() {
        assertThatThrownBy(() -> TokenUtils.validateLinkToken("x"))
                .isInstanceOf(DEException.class)
                .hasMessageContaining("token is invalid");
    }

    @Test
    void validateLinkToken_validToken_returnsTokenUserBO() {
        String token = createValidToken(42L, 10L);

        TokenUserBO result = TokenUtils.validateLinkToken(token);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(42L);
        assertThat(result.getDefaultOid()).isEqualTo(10L);
    }
}
