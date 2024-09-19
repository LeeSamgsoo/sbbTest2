package com.example.demo.article;

import com.example.demo.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Page<Article> getArticles(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.articleRepository.findAllByKeyword(kw, pageable);
    }

    public void create(String title, String content, SiteUser user) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setWriter(user);
        article.setCreateDate(LocalDateTime.now());
        this.articleRepository.save(article);
    }

    public Article getArticle(Integer id) {
        return this.articleRepository.findById(id).orElse(null);
    }

    public void modify(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        article.setModifyDate(LocalDateTime.now());
        this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }
}
