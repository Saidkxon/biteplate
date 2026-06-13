package com.biteplate.pattern.command;

public interface KitchenCommand {
    void execute();
    void undo();
    String getOrderId();
    String getDescription();
}
