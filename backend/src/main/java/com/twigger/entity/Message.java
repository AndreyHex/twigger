package com.twigger.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 333)
    @JsonView({View.UserWithMessages.class, View.Message.class})
    private String text;

    @Size(max = 16)
    @Column(unique = true)
    @JsonView(View.Message.class)
    private String publicId;

    @JsonView({View.UserWithMessages.class, View.Message.class})
    private Instant postDate;

    @ManyToOne
    @JoinColumn(name = "usr_id", updatable = false)
    @JsonView(View.MessageWithUser.class)
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getPostDate() {
        return postDate;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
