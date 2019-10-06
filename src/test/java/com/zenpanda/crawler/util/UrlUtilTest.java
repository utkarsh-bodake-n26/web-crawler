package com.zenpanda.crawler.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlUtilTest {

    @Nested
    class ValidateUrl {

        @Test
        void shouldValidateInValidUrl1() {
            assertFalse(UrlUtil.isValidUrl(null));
        }

        @Test
        void shouldValidateInValidUrl2() {
            assertFalse(UrlUtil.isValidUrl(""));
        }

        @Test
        void shouldValidateInValidUrl3() {
            assertFalse(UrlUtil.isValidUrl("google"));
        }

        @Test
        void shouldValidateInValidUrl4() {
            assertFalse(UrlUtil.isValidUrl("google.com/abc"));
        }

        @Test
        void shouldValidateValidUrl1() {
            assertTrue(UrlUtil.isValidUrl("http://google.com"));
        }

        @Test
        void shouldValidateValidUrl2() {
            assertTrue(UrlUtil.isValidUrl("http://www.google.com"));
        }

        @Test
        void shouldValidateValidUrl3() {
            assertTrue(UrlUtil.isValidUrl("http://www.google.com/abc"));
        }
    }

    @Nested
    class MatchDomain {

        @Test
        void shouldMatchUrlsWithSameDomain1() throws URISyntaxException {
            assertTrue(UrlUtil.isSameDomain("http://google.com", "http://www.google.com/a"));
        }

        @Test
        void shouldMatchUrlsWithSameDomain2() throws URISyntaxException {
            assertTrue(UrlUtil.isSameDomain("https://google.com", "http://www.google.com/a"));
        }

        @Test
        void shouldThrowException1() {
            assertThrows(URISyntaxException.class, () -> {
                UrlUtil.isSameDomain("google.com", "http://www.google.com/a");
            });
        }

        @Test
        void shouldThrowException2() {
            assertThrows(URISyntaxException.class, () -> {
                UrlUtil.isSameDomain("www.google.com", "http://www.google.com/a");
            });
        }

        @Test
        void shouldThrowException3() {
            assertThrows(URISyntaxException.class, () -> {
                UrlUtil.isSameDomain("", "http://www.google.com/a");
            });
        }
    }
}
