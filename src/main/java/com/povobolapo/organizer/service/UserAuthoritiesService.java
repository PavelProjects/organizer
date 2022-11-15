package com.povobolapo.organizer.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthoritiesService {
    private static final String READ = "READ_ALL";
    private static final String DELETE = "DELETE_ALL";
    private static final String MODIFY = "MODIFY_ALL";

    /**
     * Возвращает логин текущего авторизованного юзера
     * @return логин пользователя
     * @throws AuthenticationException - если текущий юзер каким-то образом был не авторизован
     */

    public static String getCurrentUserLogin() throws AuthenticationException {
        return getCurrentUser().getName();
    }

    /**
     * Проверят наличие доступа текущего юзера к данным юзера userLogin
     * @param userLogin - логин юзера, доступ к данным которого хотим проверить
     * @return - boolean может ли текущий юзер читать данные юзера userLogin
     * @throws AuthenticationException - если текущий юзер каким-то образом был не авторизован
     */
    public static boolean canReadUserData(String userLogin) throws AuthenticationException {
        return isSameUser(userLogin) || checkAuthority(Set.of(READ));
    }

    /**
     * Проверят наличие прав у текущего юзера для изменения данных юзера userLogin
     * @param userLogin - логин юзера, данные которого хочет изменить текущий юзер
     * @return - boolean может ли текущий юзер изменять данные юзера userLogin
     * @throws AuthenticationException - если текущий юзер каким-то образом был не авторизован
     */
    public static boolean canModifyUserData(String userLogin) throws AuthenticationException {
        return isSameUser(userLogin) || checkAuthority(Set.of(READ, MODIFY));
    }

    /**
     * Проверят наличие прав у текущего юзера для удаления данных юзера userLogin
     * @param userLogin - логин юзера, данные которого хочет удалить текущий юзер
     * @return - boolean может ли текущий юзер удалить данные юзера userLogin
     * @throws AuthenticationException - если текущий юзер каким-то образом был не авторизован
     */
    public static boolean canDeleteUserData(String userLogin) throws AuthenticationException {
        return isSameUser(userLogin) || checkAuthority(Set.of(READ, MODIFY, DELETE));
    }

    /**
     * Проверяет наличие прав у текущего юзера
     * @param authorities - набор прав, которые должны быть у юзера для получения доступа
     * @return
     * @throws AuthenticationException
     */
    public static boolean checkAuthority(Collection<String> authorities) throws AuthenticationException {
        Collection<? extends GrantedAuthority> userAuthorities = getCurrentUser().getAuthorities();
        if (userAuthorities == null || userAuthorities.isEmpty() || userAuthorities.size() < authorities.size()) {
            return false;
        }
        return authorities.containsAll(userAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    private static boolean isSameUser(String userLogin) throws AuthenticationException {
        return StringUtils.equals(userLogin, getCurrentUser().getName());
    }

    private static Authentication getCurrentUser() throws AuthenticationException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || StringUtils.isBlank(currentUser.getName())) {
            throw new AuthenticationException("Current user didn't authenticate!");
        }
        return currentUser;
    }
}
