package com.xlf.employee.api;

import com.xlf.employee.pojo.EmployeeActivity;

public interface IEmployeeActivityService {

    EmployeeActivity useToilet(Long employeeId);

    EmployeeActivity restoreToilet(Long employeeId);

    void demo();

}
