package com.povobolapo.organizer;

import com.povobolapo.organizer.config.JwtTokenUtil;
import com.povobolapo.organizer.controller.model.TaskRequestBody;
import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.TaskService;
import com.povobolapo.organizer.service.UserDetailsServiceImpl;
import com.povobolapo.organizer.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = OrganizerApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrganizerApplicationTests {
	private static final String TEST_USER_LOGIN = "autotest_user_2";

    @Autowired
    private TaskService taskService;
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Test
	@Order(1)
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
		assertTrue(StringUtils.isNotBlank(user.getId()));

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
	void testCRUDTask() {
		TaskRequestBody taskRequest = new TaskRequestBody("test_task_CRUD", "I am trying to test methods",
				"autotest_user");

		//Создаем таску
		System.out.println("Create task.");
		TaskEntity createdTask = taskService.createNewTask(taskRequest.getAuthor(), taskRequest);
		//Получаем таску
		System.out.println("Get task.");
		TaskEntity arrivedTask = taskService.getTaskById(createdTask.getId());
		//Сравниваем
		assert createdTask.equals(arrivedTask);

		final String TEST_DESCRIPTION = "I am just testing";
		//Обновляем таску
		System.out.println("Update task.");
		System.out.println("TASK IS: " + arrivedTask);
		TaskEntity updatedTask = taskService.updateTask(new TaskRequestBody(createdTask.getId(), TEST_DESCRIPTION));
		arrivedTask = taskService.getTaskById(updatedTask.getId());

		//Проверяем, что id не менялись
		System.out.println("Check after update.");
		assert createdTask.getId().equals(updatedTask.getId());
		assert updatedTask.getId().equals(arrivedTask.getId());
		//Проверяем, что поменялось описание
		assert arrivedTask.getDescription().equals(TEST_DESCRIPTION);

		//Удаляем таску
		System.out.println("Delete task.");
		taskService.deleteTaskById(createdTask.getId());
		//Проверяем, что таски нет
		System.out.println("Final check.");
		try {
			taskService.getTaskById(updatedTask.getId());
		} catch (NotFoundException exc){
		}
	}

	private void setSecurityContext(String login) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(login);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}
}
