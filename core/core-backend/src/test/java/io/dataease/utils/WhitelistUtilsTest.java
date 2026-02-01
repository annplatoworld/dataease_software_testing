package io.dataease.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

@Tag("WhitelistUtils")
class WhitelistUtilsTest {

    @Test
    void match_loginLocalLogin_returnsTrue() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/login/localLogin")).isTrue();
        }
    }

    @Test
    void match_withApiPrefix_loginLocalLogin_returnsTrue() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/de2api/login/localLogin")).isTrue();
        }
    }

    @Test
    void match_protectedPath_returnsFalse() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/de2api/datasetTree/list")).isFalse();
        }
    }

    @Test
    void match_staticResource_returnsTrue() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/de2api/static-resource/foo")).isTrue();
        }
    }

    @Test
    void match_jsFile_returnsTrue() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/de2api/app.js")).isTrue();
        }
    }

    @Test
    void match_shareProxyInfo_returnsTrue() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/de2api/share/proxyInfo")).isTrue();
        }
    }

    @Test
    void match_mfaLogin_returnsTrue() {
        try (MockedStatic<WhitelistUtils> wl = mockStatic(WhitelistUtils.class)) {
            wl.when(WhitelistUtils::getContextPath).thenReturn("");
            wl.when(() -> WhitelistUtils.match(anyString())).thenCallRealMethod();

            assertThat(WhitelistUtils.match("/de2api/mfa/login")).isTrue();
        }
    }
}
