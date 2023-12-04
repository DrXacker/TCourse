package com.example.TCourse;

import com.example.TCourse.dto.TaskDto;
import com.example.TCourse.entity.Task;
import com.example.TCourse.entity.constant.Category;
import com.example.TCourse.entity.constant.Priority;
import com.example.TCourse.exception.NotFoundException;
import com.example.TCourse.repository.TaskRepository;
import com.example.TCourse.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TCourseApplicationTestsTask {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void getTaskDtoById_ExistingTask_ReturnsTaskDto() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task(taskId, "some", "misha", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TaskDto expectedTaskDto = new TaskDto(taskId, "some", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        TaskDto result = taskService.getTaskDtoById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTaskDto, result);
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getTaskDtoById_InvalidId_TaskNotFound() {
        // Arrange
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(NotFoundException.class, () -> {
            taskService.getTaskDtoById(taskId);
        });
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getUserTasks_ReturnsListOfTaskDtos() {
        // Arrange
        Principal principal = mock(Principal.class);
        String username = "misha";

        Task task1 = new Task(1L, "some1", username, false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Task task2 = new Task(2L, "some2", username, false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        TaskDto taskDto1 = new TaskDto(1L, "some1", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TaskDto taskDto2 = new TaskDto(2L, "some2", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        List<TaskDto> taskDtos = new ArrayList<>();
        taskDtos.add(taskDto1);
        taskDtos.add(taskDto2);
        when(principal.getName()).thenReturn(username);
        when(taskRepository.findAllByUsername(username)).thenReturn(tasks);

        List<TaskDto> result = taskService.getUserTasks(principal);

        // Assert
        Assertions.assertEquals(taskDtos.size(), result.size());
        verify(principal, times(1)).getName();
        verify(taskRepository, times(1)).findAllByUsername(username);
    }

    @Test
    void createTask_ValidPrincipalAndTaskDto_TaskCreated() {
        // Arrange
        Principal principal = mock(Principal.class);
        String username = "misha";

        Task task = new Task(1L, "some", username, false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TaskDto taskDto = new TaskDto(1L, "some", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        List<Task> userTasks = new ArrayList<>();

        Task userTask = new Task(1L, "sometest", username + "test", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        userTasks.add(userTask);

        List<TaskDto> userTaskDtos = new ArrayList<>();

        TaskDto userTaskDto = new TaskDto(1L, "sometest", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        userTaskDtos.add(userTaskDto);

        when(principal.getName()).thenReturn(username);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findAllByUsername(username)).thenReturn(userTasks);

        // Act
        List<TaskDto> result = taskService.createTask(principal, taskDto);

        // Assert
        Assertions.assertEquals(userTaskDtos.size(), result.size());
        verify(principal, times(2)).getName();
        verify(taskRepository, times(1)).save(task);
        verify(taskRepository, times(1)).findAllByUsername(username);
    }

    @Test
    void updateTask_ValidPrincipalAndTaskIdAndTaskDto_TaskUpdated() {
        // Arrange
        Principal principal = mock(Principal.class);
        String username = "misha";
        Long taskId = 1L;

        Task task = new Task(taskId, "some", username, false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TaskDto taskDto = new TaskDto(taskId, "some", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        List<Task> userTasks = new ArrayList<>();
        userTasks.add(task);

        List<TaskDto> userTaskDtos = new ArrayList<>();
        TaskDto updTaskDto = new TaskDto(2L, "some2", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        userTaskDtos.add(updTaskDto);

        when(principal.getName()).thenReturn(username);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findAllByUsername(username)).thenReturn(userTasks);

        // Act
        List<TaskDto> result = taskService.updateTask(principal, taskId, taskDto);

        // Assert
        Assertions.assertEquals(userTaskDtos.size(), result.size());
        verify(principal, times(2)).getName();
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(task);
        verify(taskRepository, times(1)).findAllByUsername(username);
    }

    @Test
    void updateTask_InvalidPrincipal_InsufficientRights() {
        // Arrange
        Principal principal = mock(Principal.class);
        String username = "misha";
        Long taskId = 1L;
        Task task = new Task(taskId, "some", username + "test", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TaskDto taskDto = new TaskDto(taskId, "some", false, Priority.Low,
                Category.Homework, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(principal.getName()).thenReturn(username);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act and Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(principal, taskId, taskDto);
        });
        verify(principal, times(1)).getName();
        verify(taskRepository, times(1)).findById(taskId);
    }
}