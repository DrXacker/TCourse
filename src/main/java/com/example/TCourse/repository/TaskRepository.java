package com.example.TCourse.repository;

import com.example.TCourse.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findAllByUsername(String username);

    boolean deleteByUsernameAndId(String username, Long id);
}
