package com.example.Foundation.service;

import com.example.Foundation.modal.Articles;
import com.example.Foundation.repositories.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArticlesService {
    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Value("${aws.s3.ArticleFolder}")
    private String folderName;

    private final ArticlesRepository articlesRepository;

    public ArticlesService(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public Articles saveArticle(Articles article, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            article.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return articlesRepository.save(article);
    }

    public List<Articles> getAllArticles() {
        return articlesRepository.findAll();
    }

    public Optional<Articles> getArticleById(int articleId) {
        return articlesRepository.findById(articleId);
    }

    public Articles updateArticle(int articleId, Articles articles, MultipartFile file) throws IOException {
        Articles articlesX = articlesRepository.findById(articleId).orElseThrow(() ->new RuntimeException("article not found"));
        if (articles.getTitle() != null) {
            articlesX.setTitle(articles.getTitle());
        }
        if (articles.getDescription() != null) {
            articlesX.setDescription(articles.getDescription());
        }
        if (articles.getCategoryType() != null) {
            articlesX.setCategoryType(articles.getTitle());
        }

        if (file != null && !file.isEmpty()) {
            String oldImageName = articles.getImage();
            String newImageName = file.getOriginalFilename();
            articlesX.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return articlesRepository.save(articlesX);
    }

    public void deleteArticle(int articleId) {
        articlesRepository.deleteById(articleId);
    }
}
