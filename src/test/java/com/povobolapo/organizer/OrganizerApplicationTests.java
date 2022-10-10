package com.povobolapo.organizer;

import com.povobolapo.organizer.config.JwtTokenUtil;
import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.NotificationEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.NotificationService;
import com.povobolapo.organizer.service.TTaskService;
import com.povobolapo.organizer.service.UserDetailsServiceImpl;
import com.povobolapo.organizer.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTests {
	private static final String TEST_USER_LOGIN = "autotest_user_2";
	private static final String AUTOTEST_LOGIN = "autotest_user";

    @Autowired
    private TTaskService service;
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private NotificationService notificationService;

	@Test
	public void jwtTest() {
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername("autotest_user");

		final String token = jwtTokenUtil.generateToken(userDetails);
		System.out.println(token);
	}

	@Test
	void checkCurrentUser() throws AuthenticationException {
		// Пробуем получить текущего юзера, когда никто не авторизовался
		try {
			userService.authenticatedUserName();
		} catch (AuthenticationException exc) {
			assert !exc.getMessage().isEmpty();
		}

		// Проверяем, что при авторизации мы получим правильного юзера
		setSecurityContext(AUTOTEST_LOGIN);
		assert StringUtils.equals(AUTOTEST_LOGIN, userService.authenticatedUserName());
	}

	@Test
	@Transactional
	void testManageUser() throws AuthenticationException{
		// Создаем юзера
		UserEntity user = userService.createUser(new UserRequestBody(TEST_USER_LOGIN, "1", "bombastik"));
		assert user.getId() > 0;

		// Делаем вид, что удалить пытается другой юзер
		setSecurityContext("autotest_user");
		try {
			userService.deleteUser(TEST_USER_LOGIN);
		} catch (AccessDeniedException exception) {
			assert exception.getMessage().equals("Permission denied!");
		}

		// Удаляем за нужного юзера
		setSecurityContext(TEST_USER_LOGIN);
		userService.deleteUser(user.getLogin());

		// Проверяем наличие ошибки, если юзер не найден
		try {
			userService.getUserByLogin(TEST_USER_LOGIN);
		} catch (NotFoundException ex) {
		}
	}

	@Test
	@Transactional
	void testNotifications() throws AuthenticationException {
		// Создаем тестовые уведомления
		notificationService.createSystemNotification(AUTOTEST_LOGIN, "unit_test_1");

		// Получаем все уведомлени юзера и убеждаемся, что наши есть в результате
		List<NotificationEntity> notifications = notificationService.getUserNotifications(AUTOTEST_LOGIN);
		assert notifications != null && !notifications.isEmpty();
		List<Integer> ids = notifications.stream().filter(notification -> StringUtils.equals(notification.getBody(), "unit_test_1") && !notification.isChecked()).map(NotificationEntity::getId).collect(Collectors.toList());
		assert ids.size() == 1;

		// Помечаем уведомление просмотренным и проверяем, что в бд оно обновилось
		notificationService.markNotificationsChecked(ids);
		notifications = notificationService.getUserNotifications(AUTOTEST_LOGIN);
		assert notifications.stream().filter(notification -> StringUtils.equals(notification.getBody(), "unit_test_1") && notification.isChecked()).count() == 1;

		// Авторизуемся под юзером, удаляем наше уведомление и проверяем, что бы оно действительно удалилось
		setSecurityContext(AUTOTEST_LOGIN);
		notificationService.deleteNotificationsByIds(ids);
		notifications = notificationService.getUserNotifications(AUTOTEST_LOGIN);
		assert notifications.stream().noneMatch(notification -> StringUtils.equals(notification.getBody(), "unit_test_1") && notification.isChecked());
	}

	private void setSecurityContext(String login) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(login);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}
}
