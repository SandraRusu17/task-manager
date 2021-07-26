package com.stefanini.taskmanager;

import com.stefanini.taskmanager.engine.TaskManager;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        new TaskManager().parseCommandLine(args);
    }
}
