package com.povobolapo.organizer;

import com.povobolapo.organizer.controller.models.UserRequestBody;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.TaskService;
import com.povobolapo.organizer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTests {
    @Autowired
    private TaskService service;
	@Autowired
	private UserService userService;

	@Test
	void testCreateDeleteUser() {
		UserEntity user = userService.createUser(new UserRequestBody("test_user", "123qwery", "heh"));
		assert user.getId() > 0;
		assert userService.deleteUser(user.getLogin());
	}

/*	@Test
	void testCreateTask() {
		TaskEntity created = service.createNewTask("test_user", new TaskEntity("test", "new"));
	}*/
}
