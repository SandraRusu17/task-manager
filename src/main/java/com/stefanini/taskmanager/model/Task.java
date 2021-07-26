package com.stefanini.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;

}
