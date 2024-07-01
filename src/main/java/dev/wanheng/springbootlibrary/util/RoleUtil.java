package dev.wanheng.springbootlibrary.util;

import dev.wanheng.springbootlibrary.domain.User;

import java.util.Objects;

import static dev.wanheng.springbootlibrary.common.LibraryConstants.*;

public class RoleUtil {

    public static boolean isAdmin(User user) {
        return Objects.equals(user.getRole(), ROLE_ADMIN_NUM);
    }

    public static boolean isReader(User user) {
        return Objects.equals(user.getRole(), ROLE_READER_NUM);
    }

}
