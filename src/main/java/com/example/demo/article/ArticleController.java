package com.example.demo.article;

import com.example.demo.user.SiteUser;
import com.example.demo.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String articleList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Article> articles = this.articleService.getArticles(page, kw);
        model.addAttribute("articlePage", articles);
        model.addAttribute("kw", kw);
        return "article_list";
    }

    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String articleCreate(ArticleForm articleForm, Model model, HttpSession session) {
        // CSRF 토큰 생성
        String csrfToken = UUID.randomUUID().toString();
        // 세션에 토큰 저장
        session.setAttribute("csrfToken", csrfToken);
        // 모델에 토큰을 전달하여 폼에서 사용
        model.addAttribute("csrfToken", csrfToken);
        return "article_form";
    }

    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                HttpSession session, HttpServletRequest request) {
        // 폼에서 전송된 CSRF 토큰
        String formCsrfToken = request.getParameter("csrfToken");

        // 세션에 저장된 CSRF 토큰
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");

        // CSRF 토큰 검증
        if (sessionCsrfToken == null || !sessionCsrfToken.equals(formCsrfToken)) {
            // 토큰이 일치하면 처리 진행
            // 비즈니스 로직 처리
            return "article_form";
        }
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        SiteUser user = this.userService.getUser(session.getAttribute("username").toString());
        if (user == null) {
            bindingResult.reject("user not found", "사용자를 찾을 수 없습니다.");
            return "article_form";
        }
        this.articleService.create(articleForm.getTitle(), articleForm.getContent(), user);
        return "redirect:/article/list";
    }

    @GetMapping("/detail/{id}")
    public String articleDetail(@PathVariable(value = "id") Integer id, Model model) {
        Article article = this.articleService.getArticle(id);
        if (article == null) {
            return "redirect:/article/list";
        }
        model.addAttribute("article", article);
        return "article_detail";
    }

    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String articleModify(ArticleForm articleForm, @PathVariable(value = "id") Integer id,
                                HttpSession session, Model model) {
        Article article = this.articleService.getArticle(id);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시물이 존재하지 않습니다.");
        }
        if (!session.getAttribute("username").toString().equals(article.getWriter().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        // CSRF 토큰 생성
        String csrfToken = UUID.randomUUID().toString();
        // 세션에 토큰 저장
        session.setAttribute("csrfToken", csrfToken);
        // 모델에 토큰을 전달하여 폼에서 사용
        model.addAttribute("csrfToken", csrfToken);

        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());
        return "article_form";
    }

    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String articleModify(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                @PathVariable(value = "id") Integer id, HttpServletRequest request) {
        // 폼에서 전송된 CSRF 토큰
        String formCsrfToken = request.getParameter("csrfToken");

        // 세션에 저장된 CSRF 토큰
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");

        // CSRF 토큰 검증
        if (sessionCsrfToken == null || !sessionCsrfToken.equals(formCsrfToken)) {
            // 토큰이 일치하면 처리 진행
            // 비즈니스 로직 처리
            return "article_form";
        }
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        Article article = this.articleService.getArticle(id);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시물이 존재하지 않습니다.");
        }
        if (!request.getSession().getAttribute("username").toString().equals(article.getWriter().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.articleService.modify(article, articleForm.getTitle(), articleForm.getContent());
        return "redirect:/article/detail/" + id;
    }

    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String articleDelete(@PathVariable(value = "id") Integer id, HttpSession session) {
        Article article = this.articleService.getArticle(id);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시물이 존재하지 않습니다.");
        }
        if (!session.getAttribute("username").toString().equals(article.getWriter().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.articleService.delete(article);
        return "redirect:/article/list";
    }
}
