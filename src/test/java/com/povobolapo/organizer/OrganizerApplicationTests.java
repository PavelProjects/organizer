package com.povobolapo.organizer;

import com.povobolapo.organizer.config.JwtTokenUtil;
import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.TTaskService;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.service.UserDetailsServiceImpl;
import com.povobolapo.organizer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;


@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTests {
    @Autowired
    private TaskService service;
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
	void testCreateDeleteUser() {
		UserEntity user = userService.createUser(new UserRequestBody("autotest_user_2", "1", "bombastik"));
		assert user.getId() > 0;
		assert userService.deleteUser(user.getLogin());
	}

  @Test
	void testCreateTask() {
		TaskEntity created = service.createNewTask("autotest_user", new TaskEntity("test", "new"));
	}
}
