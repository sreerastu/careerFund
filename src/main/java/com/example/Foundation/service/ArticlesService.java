package com.example.Foundation.service;

import com.example.Foundation.modal.Articles;

import com.example.Foundation.repositories.ArticlesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ArticlesService {

    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    private final ArticlesRepository articlesRepository;

    public ArticlesService(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public Articles saveArticle(Articles article, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            article.setImage(fileName);
        }
        return articlesRepository.save(article);
    }

    public List<Articles> getAllArticles() {
        return articlesRepository.findAll();
    }

    public Optional<Articles> getArticleById(int articleId) {
        return articlesRepository.findById(articleId);
    }

    public Articles updateArticle(int articleId, Articles updatedArticle) throws IOException {
        Optional<Articles> optionalArticle = articlesRepository.findById(articleId);
        if (optionalArticle.isPresent()) {
            Articles existingArticle = optionalArticle.get();
            existingArticle.setTitle(updatedArticle.getTitle());
            existingArticle.setDescription(updatedArticle.getDescription());
            existingArticle.setCategoryType(updatedArticle.getCategoryType());
            if (updatedArticle.getImage() != null) {
                existingArticle.setImage(updatedArticle.getImage());
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
