package com.example.demo.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<Article> findAll(Pageable pageable);
    @Query("select "
            + "distinct q "
            + "from Article q "
            + "left outer join SiteUser u1 on q.writer=u1 "
            + "where "
            + "   q.title like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% ")
    Page<Article> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
