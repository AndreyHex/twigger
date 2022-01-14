package com.twigger.entity;

public final class View {
    public interface Username {}
    public interface UserWithEmail extends Username {}
    public interface UserWithPassword extends UserWithEmail {}

    public interface UserWithMessages extends Username {}

    public interface Message {}
    public interface MessageWithUser extends Message {}
}
