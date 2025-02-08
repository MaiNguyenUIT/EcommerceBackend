package com.example.backend.utils;

import java.util.regex.Pattern;

public class ValidationAccount {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // Regex kiểm tra mật khẩu (Chữ cái in hoa đầu tiên + ít nhất 1 ký tự đặc biệt)
    private static final String PASSWORD_REGEX = "^[A-Z][A-Za-z0-9]*[!@#$%^&*()_+=<>?/{}\\[\\]-]+[A-Za-z0-9]*$";

    /**
     * Kiểm tra xem chuỗi có phải là email hợp lệ hay không.
     *
     * @param email Chuỗi email cần kiểm tra
     * @return true nếu hợp lệ, ngược lại false
     */
    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Kiểm tra mật khẩu có hợp lệ không:
     * - Bắt đầu bằng chữ cái in hoa
     * - Chứa ít nhất một ký tự đặc biệt
     *
     * @param password Chuỗi mật khẩu cần kiểm tra
     * @return true nếu hợp lệ, ngược lại false
     */
    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
}
