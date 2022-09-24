package com.povobolapo.organizer;

import com.povobolapo.organizer.dao.api.TTaskService;
import com.povobolapo.organizer.model.TTask;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@RunWith(SpringRunner.class)
class OrganizerApplicationTests {
	private TTaskService dao;
	@PersistenceContext
	private EntityManager em;

	@Autowired
	public OrganizerApplicationTests(TTaskService dao) {
		this.dao = dao;
	}

	@Test
	void testTaskDao() {
		TTask created = dao.createTask(new TTask("test", "new"));
		System.out.println(created);
	}

}
