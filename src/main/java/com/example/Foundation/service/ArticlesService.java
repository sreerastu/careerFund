package com.example.Foundation.service;

import com.example.Foundation.modal.Articles;

import com.example.Foundation.repositories.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArticlesService {

    private final ArticlesRepository articlesRepository;

    @Autowired
    public ArticlesService(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public Articles saveArticle(Articles article, MultipartFile image) throws IOException {
        if (image != null) {
            article.setImage(image.getBytes());
        }
        return articlesRepository.save(article);
    }

    public List<Articles> getAllArticles() {
        return articlesRepository.findAll();
    }

    public Optional<Articles> getArticleById(int articleId) {
        return articlesRepository.findById(articleId);
    }

    public Articles updateArticle(int articleId, Articles updatedArticle, MultipartFile image) throws IOException {
        Optional<Articles> optionalArticle = articlesRepository.findById(articleId);
        if (optionalArticle.isPresent()) {
            Articles existingArticle = optionalArticle.get();
            existingArticle.setTitle(updatedArticle.getTitle());
            existingArticle.setDescription(updatedArticle.getDescription());
            existingArticle.setCategoryType(updatedArticle.getCategoryType());
            if (image != null) {
                existingArticle.setImage(image.getBytes());
            }
            return articlesRepository.save(existingArticle);
        } else {
            throw new IllegalArgumentException("Article not found with ID: " + articleId);
        }
    }

    public void deleteArticle(int articleId) {
        articlesRepository.deleteById(articleId);
    }
}
