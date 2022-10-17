package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.DictNotifyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyTypeRepository extends JpaRepository<DictNotifyType, Integer> {
    DictNotifyType findByName(String name);
}
