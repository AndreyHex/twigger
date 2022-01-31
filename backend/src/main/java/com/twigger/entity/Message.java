package com.twigger.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 333)
    @JsonView(View.Message.class)
    private String text;

    @Size(max = 16)
    @Column(unique = true)
    @JsonView(View.Message.class)
    private String publicId;

    @JsonView(View.Message.class)
    private Instant postDate;

    @ManyToOne
    @JoinColumn(name = "usr_id", updatable = false)
    @JsonView(View.FullMessage.class)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonView(View.FullMessage.class)
    @JsonIgnoreProperties("responseTo")
    private Message responseTo;
    @OneToMany(mappedBy = "responseTo")
    @JsonView(View.FullMessageWithResponses.class)
    @JsonIgnoreProperties("responses")
    private List<Message> responses;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonView(View.Message.class)
    @JsonIgnoreProperties("repostedMessage")
    private Message repostedMessage;

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

    public Message getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(Message responseTo) {
        this.responseTo = responseTo;
    }

    public List<Message> getResponses() {
        return responses;
    }

    public void setResponses(List<Message> responses) {
        this.responses = responses;
    }

    public Message getRepostedMessage() {
        return repostedMessage;
    }

    public void setRepostedMessage(Message repostedMessage) {
        this.repostedMessage = repostedMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", publicId='" + publicId + '\'' +
                ", postDate=" + postDate +
                ", user=" + user +
                ", responseTo=" + responseTo +
                ", responses=" + responses +
                ", repostedMessage=" + repostedMessage +
                '}';
    }
}
