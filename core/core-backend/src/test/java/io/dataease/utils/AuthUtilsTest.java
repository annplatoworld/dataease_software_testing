package io.dataease.utils;

import io.dataease.auth.bo.TokenUserBO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("AuthUtils")
class AuthUtilsTest {

    @AfterEach
    void tearDown() {
        AuthUtils.remove();
    }

    @Test
    void isSysAdmin_userId1_returnsTrue() {
        assertThat(AuthUtils.isSysAdmin(1L)).isTrue();
    }

    @Test
    void isSysAdmin_userId2_returnsFalse() {
        assertThat(AuthUtils.isSysAdmin(2L)).isFalse();
    }

    @Test
    void isSysAdmin_withoutUser_returnsFalse() {
        assertThat(AuthUtils.getUser()).isNull();
        assertThat(AuthUtils.isSysAdmin()).isFalse();
    }

    @Test
    void isSysAdmin_withUser1_returnsTrue() {
        AuthUtils.setUser(new TokenUserBO(1L, 1L));

        assertThat(AuthUtils.isSysAdmin()).isTrue();
    }

    @Test
    void isSysAdmin_withUser2_returnsFalse() {
        AuthUtils.setUser(new TokenUserBO(2L, 1L));

        assertThat(AuthUtils.isSysAdmin()).isFalse();
    }

    @Test
    void setUser_getUser_returnsSetUser() {
        TokenUserBO user = new TokenUserBO(100L, 5L);
        AuthUtils.setUser(user);

        assertThat(AuthUtils.getUser()).isSameAs(user);
        assertThat(AuthUtils.getUser().getUserId()).isEqualTo(100L);
        assertThat(AuthUtils.getUser().getDefaultOid()).isEqualTo(5L);
    }

    @Test
    void remove_clearsUser() {
        AuthUtils.setUser(new TokenUserBO(1L, 1L));
        assertThat(AuthUtils.getUser()).isNotNull();

        AuthUtils.remove();

        assertThat(AuthUtils.getUser()).isNull();
    }
}
