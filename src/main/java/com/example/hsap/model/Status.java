package com.example.hsap.model;

public enum Status {

    ACTIVE(0, "활성화"),
    SPECIAL(1, "특수상황"),
    JOURNEY(2, "영적여정"),
    INACTIVE(3, "제적대상");

    final int level;
    final String msg;

    Status(int level, String msg) {
        this.level = level;
        this.msg = msg;
    }

}
