package com.chill.order.service.CommandPattern;

public interface Command {
    void execute();
    void undo();
}
