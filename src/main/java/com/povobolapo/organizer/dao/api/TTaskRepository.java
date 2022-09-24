package com.povobolapo.organizer.dao.api;

import com.povobolapo.organizer.model.TTask;

public interface TTaskRepository {
    TTask createTask(String name, String description);
}
