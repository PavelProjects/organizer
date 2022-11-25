package com.povobolapo.organizer;

import com.povobolapo.organizer.controller.model.user.UserRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.NotificationEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.*;
import com.povobolapo.organizer.utils.Event;
import com.povobolapo.organizer.utils.EventDispatcher;
import com.povobolapo.organizer.utils.EventHandler;
import com.povobolapo.organizer.utils.JwtTokenUtil;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTestIT {
    private static final String TEST_USER_LOGIN = "autotest_user_2";
    private static final String AUTOTEST_LOGIN = "autotest_user";
    private static final String BASIC_USER_LOGIN = "basic_user";

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CommentService commentService;

    @Test
    public void authTest() {
        String token = jwtTokenUtil.generateToken(AUTOTEST_LOGIN);
        assert StringUtils.isNotBlank(token);
        assert null != jwtTokenUtil.validateToken(token);
    }

    @Test
    void checkCurrentUser() throws AuthenticationException {
        // Пробуем получить текущего юзера, когда никто не авторизовался
        try {
            UserAuthoritiesService.getCurrentUserLogin();
        } catch (AuthenticationException exc) {
            assert !exc.getMessage().isEmpty();
        }

        // Проверяем, что при авторизации мы получим правильного юзера
        setSecurityContext(AUTOTEST_LOGIN);
        assert StringUtils.equals(AUTOTEST_LOGIN, UserAuthoritiesService.getCurrentUserLogin());
        UserEntity current = userService.getCurrentUser();
        assert current != null && StringUtils.equals(current.getLogin(), AUTOTEST_LOGIN);
    }

    @Test
    @Transactional
    void testManageUser() throws AuthenticationException {
        // Создаем юзера
        UserEntity user = userService.createUser(new UserRequestBody(
                TEST_USER_LOGIN, "1", "jopa@mail.ru", "bombastik", ""));
        assertTrue(StringUtils.isNotBlank(user.getId()));

        // Делаем вид, что удалить пытается другой юзер
        setSecurityContext(BASIC_USER_LOGIN);
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
    void testNotifications() throws AuthenticationException {
        setSecurityContext(AUTOTEST_LOGIN);

        // Создаем тестовые уведомления
        notificationService.createNotification(userService.getUserByLogin(AUTOTEST_LOGIN), null, "unit_test_1", NotificationService.NotifyTypes.SYSTEM);

        // Получаем все уведомлени юзера и убеждаемся, что наши есть в результате
        List<NotificationEntity> notifications = notificationService.getUserNotifications();
        assert notifications != null && !notifications.isEmpty();
        List<String> ids = notifications.stream().filter(notification -> StringUtils.equals(notification.getBody(), "unit_test_1")
                && !notification.isChecked()).map(NotificationEntity::getId).collect(Collectors.toList());
        assert ids.size() == 1;

        // Помечаем уведомление просмотренным и проверяем, что в бд оно обновилось
        notificationService.markNotificationsChecked(ids);
        notifications = notificationService.getUserNotifications();
        assert notifications.stream().filter(notification -> StringUtils.equals(notification.getBody(), "unit_test_1")
                && notification.isChecked()).count() == 1;

        notificationService.deleteNotificationsByIds(ids);
        notifications = notificationService.getUserNotifications();
        assert notifications.stream().noneMatch(notification -> StringUtils.equals(notification.getBody(), "unit_test_1")
                && notification.isChecked());
    }

    //TODO ПЕРЕДЕЛАТЬ. Печатать в консоль ничего не надо.
//	void testCRUDTask() {
//		TaskRequestBody taskRequest = new TaskRequestBody("test_task_CRUD", "I am trying to test methods",
//				"autotest_user");
//
//		//Создаем таску
//		System.out.println("Create task.");
//		TaskEntity createdTask = taskService.createNewTask(taskRequest);
//		//Получаем таску
//		System.out.println("Get task.");
//		TaskEntity arrivedTask = taskService.getTaskById(createdTask.getId());
//		//Сравниваем
//		assert createdTask.equals(arrivedTask);
//
//		final String TEST_DESCRIPTION = "I am just testing";
//		//Обновляем таску
//		System.out.println("Update task.");
//		System.out.println("TASK IS: " + arrivedTask);
//		TaskEntity updatedTask = taskService.updateTask(new TaskRequestBody(createdTask.getId(), TEST_DESCRIPTION));
//		arrivedTask = taskService.getTaskById(updatedTask.getId());
//
//		//Проверяем, что id не менялись
//		System.out.println("Check after update.");
//		assert createdTask.getId().equals(updatedTask.getId());
//		assert updatedTask.getId().equals(arrivedTask.getId());
//		//Проверяем, что поменялось описание
//		assert arrivedTask.getDescription().equals(TEST_DESCRIPTION);
//
//		//Удаляем таску
//		System.out.println("Delete task.");
//		taskService.deleteTaskById(createdTask.getId());
//		//Проверяем, что таски нет
//		System.out.println("Final check.");
//		try {
//			taskService.getTaskById(updatedTask.getId());
//		} catch (NotFoundException exc){
//		}
//	}


    private void setSecurityContext(String login) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Test
    void testDispatcher() {
        EventDispatcher eventDispatcher = new EventDispatcher();
        DummyHandler h = new DummyHandler();
        eventDispatcher.registerHandler(DummyEvent.class, (EventHandler<DummyEvent>) h::onEvent);
        try {
            eventDispatcher.dispatch(new DummyEvent());
        } catch (Exception ex) {
            assert ex.getMessage().equals("yeah");
        }
    }

    private class DummyEvent implements Event {
    }

    private class DummyHandler implements EventHandler<DummyEvent> {
        @Override
        public void onEvent(DummyEvent event) throws Exception {
            throw new RuntimeException("yeah");
        }
    }
}
