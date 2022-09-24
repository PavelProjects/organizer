package com.povobolapo.organizer.dao.api;

import com.povobolapo.organizer.model.DictTaskStatus;

public interface TaskStatusService {
    DictTaskStatus getTaskStatus(String name);
}
