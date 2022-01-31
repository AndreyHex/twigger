package com.twigger.entity;

public final class View {
    public interface Username {}
    //username
    public interface UserWithEmail extends Username {}
    //+email
    public interface UserWithPassword extends UserWithEmail {}
    //+password
    public interface UserWithMessages extends Username,Message {}
    //+messages

    public interface Message {}
    //text
    //public id
    //post date
    //reposted message
    public interface FullMessage extends Message, Username {}
    //+responseto message
    public interface FullMessageWithResponses extends FullMessage {}
    //+list<message>

}
