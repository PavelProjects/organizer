package com.povobolapo.organizer;

import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.TTaskService;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTests {
    @Autowired
    private TTaskService service;
	@Autowired
	private UserService userService;

	@Test
	void testCreateDeleteUser() {
		UserEntity user = userService.createUser("test_user", "1", "test user");
		assert user.getId() > 0;
		assert userService.deleteUser(user.getLogin());
	}

	@Test
	void testCreateTask() {
		TaskEntity created = service.createNewTask("test_user", new TaskEntity("test", "new"));
	}
}
