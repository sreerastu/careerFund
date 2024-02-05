package com.example.Foundation.controller;

import com.example.Foundation.modal.Articles;
import com.example.Foundation.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticlesController {

    private final ArticlesService articlesService;

    @Autowired
    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveArticle(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryType") String categoryType,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        Articles article = new Articles();
        article.setTitle(title);
        article.setDescription(description);
        article.setCategoryType(categoryType);

        Articles savedArticle = articlesService.saveArticle(article, image);
        return ResponseEntity.ok("Article saved with ID: " + savedArticle.getArticleId());
    }


    @GetMapping("/all")
    public ResponseEntity<List<Articles>> getAllArticles() {
        List<Articles> articles = articlesService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Articles> getArticleById(@PathVariable int articleId) {
        Optional<Articles> optionalArticle = articlesService.getArticleById(articleId);
        return optionalArticle.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<String> updateArticle(@PathVariable int articleId, @ModelAttribute Articles updatedArticle, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Articles updated = articlesService.updateArticle(articleId, updatedArticle, image);
        return ResponseEntity.ok("Article updated with ID: " + updated.getArticleId());
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticle(@PathVariable int articleId) {
        articlesService.deleteArticle(articleId);
        return ResponseEntity.ok("Article deleted with ID: " + articleId);
    }
}
