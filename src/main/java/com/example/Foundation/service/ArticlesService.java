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
    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    //  private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    private final ArticlesRepository articlesRepository;

    public ArticlesService(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public Articles saveArticle(Articles article, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            article.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(fileName, file);
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
