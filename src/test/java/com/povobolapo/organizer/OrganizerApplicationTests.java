package com.povobolapo.organizer;

import com.povobolapo.organizer.dao.service.TTaskService;
import com.povobolapo.organizer.model.TTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = OrganizerApplication.class)
class OrganizerApplicationTests {
    @Autowired
    private TTaskService service;

	@Test
	void testTaskDao() {
		TTask created = service.createNewTask("test_user", new TTask("test", "new"));
		System.out.println(created);
	}

}
