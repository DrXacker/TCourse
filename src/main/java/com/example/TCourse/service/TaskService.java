package com.example.TCourse.service;

import com.example.TCourse.dto.TaskDto;
import com.example.TCourse.dto.mapper.TaskMapper;
import com.example.TCourse.entity.Task;
import com.example.TCourse.exception.NotFoundException;
import com.example.TCourse.repository.TaskRepository;
import com.example.TCourse.specifications.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
    public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDto getTaskDtoById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::map)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public List<TaskDto> getUserTasks(Principal principal) {
        String username = principal.getName();
        return taskRepository.findAllByUsername(username)
                .stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    public List<TaskDto> createTask(Principal principal, TaskDto taskDto) {
        String username = principal.getName();
        Task task = taskRepository.save(taskMapper.map(taskDto, username));
        return this.getUserTasks(principal);
    }

    public List<TaskDto> updateTask(Principal principal, Long taskId, TaskDto taskDto) {
        String username = principal.getName();
        return taskRepository.findById(taskId)
                .map(task1 -> {
                    task1.setIsCompleted(taskDto.getIsCompleted());
                    return task1;
                })
                .map(taskRepository::save)
                .map(task -> this.getUserTasks(principal))
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public Boolean deleteTask(Principal principal, Long taskId){
        String username = principal.getName();
        boolean isDeleted = taskRepository.deleteByUsernameAndId(username, taskId);

        if (!isDeleted) {
            throw new NotFoundException("Task not found");
        }

        return true;
    }

    public List<TaskDto> searchTasksAllRows(Principal principal, String body, String priority, String category,
                                            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String username = principal.getName();
        return taskRepository.findAll(TaskSpecifications.searchTasksAllRows(username, body, priority, category,
                startDateTime, endDateTime))
                .stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }
}