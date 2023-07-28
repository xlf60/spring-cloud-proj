package com.xlf.employee.dao;

import com.xlf.employee.pojo.ActivityType;
import com.xlf.employee.entity.EmployeeActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeActivityDao extends JpaRepository<EmployeeActivityEntity, Long> {

    int countByEmployeeIdAndActivityTypeAndActive(Long employeeId,
                                                ActivityType activityType,
                                                boolean activity);

}
