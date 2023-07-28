package com.xlf.restroom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "toilet")
@Entity
public class ToiletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql自增
    @Column(name = "id")
    private Long id;

    @Column(name = "clean", nullable = false)
    private boolean clean;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "reserved", nullable = true)
    private boolean reserved;
}
