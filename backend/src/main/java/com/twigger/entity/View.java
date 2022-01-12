package com.twigger.entity;

public final class View {
    interface Username {}
    interface UserWithMessages extends Username {}

    public interface Message {}
    public interface MessageWithUser extends Message {}
}
