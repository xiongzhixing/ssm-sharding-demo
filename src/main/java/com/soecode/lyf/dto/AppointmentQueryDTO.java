package com.soecode.lyf.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class AppointmentQueryDTO implements Serializable {
    private static final long serialVersionUID = 5375268362926848143L;

    private Long bookId;
    private Long studentId;
    private String name;
}
