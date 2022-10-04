package com.povobolapo.organizer;

import com.povobolapo.organizer.config.JwtTokenUtil;
import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.TTaskService;
import com.povobolapo.organizer.service.UserDetailsServiceImpl;
import com.povobolapo.organizer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTests {
	private static final String TEST_USER_LOGIN = "autotest_user_2";

    @Autowired
    private TTaskService service;
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Test
	public void jwtTest() {
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername("autotest_user");

		final String token = jwtTokenUtil.generateToken(userDetails);
		System.out.println(token);
	}

	@Test
	@Transactional
	void testCreateUser() {
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
	}

	private void setSecurityContext(String login) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(login);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}
}
