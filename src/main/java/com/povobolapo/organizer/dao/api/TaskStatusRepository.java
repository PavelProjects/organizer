package com.povobolapo.organizer.dao.api;

import com.povobolapo.organizer.model.DictTaskStatus;

public interface TaskStatusRepository {
    DictTaskStatus getTaskStatus(String name);
}
