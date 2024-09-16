package com.example.demo;

import com.example.demo.article.ArticleService;
import com.example.demo.user.SiteUser;
import com.example.demo.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        SiteUser user = this.userService.getUser("test");
        for (int i = 0; i < 300; i++) {
            this.articleService.create("title"+i, "This is content.", user);
        }
    }

}
