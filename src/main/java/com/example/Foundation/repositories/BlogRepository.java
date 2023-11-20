package com.example.Foundation.repositories;

import com.example.Foundation.modal.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog,Integer> {
}
