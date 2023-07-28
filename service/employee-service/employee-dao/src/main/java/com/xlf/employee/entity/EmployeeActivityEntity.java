package com.xlf.employee.entity;

import com.xlf.employee.pojo.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.StringJoiner;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "employee_activity")
@Entity
public class EmployeeActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql自增
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.ORDINAL)
    private ActivityType activityType;

    private Long resourceId;

    @CreatedDate
    private Date startTime;

    private Date endTime;

    private boolean active;

}
