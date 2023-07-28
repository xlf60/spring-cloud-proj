package com.xlf.employee.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeActivity {

    private Long id;

    private Long employeeId;

    private ActivityType activityType;

    private Long resourceId;

    private Date startTime;

    private Date endTime;

    private boolean active;

}
