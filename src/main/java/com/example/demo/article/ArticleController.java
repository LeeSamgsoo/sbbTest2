package com.example.demo.article;

import com.example.demo.user.SiteUser;
import com.example.demo.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String articleList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Article> articlePage = this.articleService.getPage(page);
        model.addAttribute("articlePage", articlePage);
        return "article_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String articleCreate(ArticleForm articleForm) {
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        SiteUser user = this.userService.getUser(principal.getName());
        if (user == null) {
            bindingResult.reject("create failed", "존재하지 않는 유저입니다.");
            return "article_form";
        }
        this.articleService.create(articleForm.getTitle(), articleForm.getContent(), user);
        return "redirect:/article/list";
    }

    @GetMapping("/detail/{id}")
    public String articleDetail(Model model, @PathVariable(value = "id") Integer id) {
        Article article = this.articleService.getArticle(id);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시물을 찾을 수 없습니다.");
        }
        model.addAttribute("article", article);
        return "article_detail";
    }
}
