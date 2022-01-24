package com.twigger.integrationtest;

import com.twigger.entity.Message;
import com.twigger.entity.User;
import com.twigger.repository.MessageRepository;
import com.twigger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@TestConfiguration
public class GlobalTestConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    @Profile("test")
    public DataSource getTestDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:mem:jpa_jbd;DB_CLOSE_DELAY=-1");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("sa");
        return dataSourceBuilder.build();
    }

    @PostConstruct
    void initTestData() {
        Random random = new Random();
        List<User> users = IntStream.range(0, 10)
                        .mapToObj(i -> createUser("test_user_" + i, "test_password_" + i))
                        .collect(Collectors.toList());
        userRepository.saveAllAndFlush(users);
        messageRepository.saveAllAndFlush(
                IntStream.range(1, 70)
                        .mapToObj(a -> {
                                    User usr = users.get(random.nextInt(10));
                                    return createMessage(usr, "Message by "+usr.getUsername());
                                } )
                        .collect(Collectors.toList())
        );
    }

    private Message createMessage(User user, String text) {
        Message newMsg =  new Message();
        newMsg.setPostDate(LocalDateTime.now());
        newMsg.setUser(user);
        newMsg.setText(text);
        return newMsg;
    }

    private User createUser(String username, String rawpassword) {
        User user = new User(username, bCryptPasswordEncoder.encode(rawpassword));
        user.setStatus(true);
        return user;
    }

}
