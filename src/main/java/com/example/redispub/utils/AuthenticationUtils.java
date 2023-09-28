package com.example.redispub.utils;

public class AuthenticationUtils {

    public static boolean valid(String token) {
        return true;
    }

    public static Long getMemberId(String token) {
        return Long.valueOf(token);
    }
}
