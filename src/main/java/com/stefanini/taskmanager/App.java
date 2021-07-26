package com.stefanini.taskmanager;

import com.stefanini.taskmanager.engine.TaskManager;

public class App {
    public static void main(String[] args) {
        new TaskManager().parseCommandArguments(args);
    }
}
