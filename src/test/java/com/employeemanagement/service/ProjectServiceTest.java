package com.employeemanagement.service;

import com.employeemanagement.dto.request.ProjectDTO;
import com.employeemanagement.dto.response.ProjectResponse;
import com.employeemanagement.entity.Department;
import com.employeemanagement.entity.Project;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private ProjectService projectService;

    private Department department;
    private Project project;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .name("Engineering")
                .location("New York")
                .build();

        project = Project.builder()
                .id(1L)
                .name("Project Alpha")
                .description("Core platform")
                .department(department)
                .build();

        projectDTO = ProjectDTO.builder()
                .name("Project Alpha")
                .description("Core platform")
                .departmentId(1L)
                .build();
    }

    @Test
    void findAll_shouldReturnAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectResponse> result = projectService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project Alpha", result.get(0).getName());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void findById_whenProjectExists_shouldReturnProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse result = projectService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Project Alpha", result.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenProjectNotFound_shouldThrowResourceNotFoundException() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.findById(999L));
    }

    @Test
    void create_shouldSaveAndReturnProject() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse result = projectService.create(projectDTO);

        assertNotNull(result);
        assertEquals("Project Alpha", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void update_shouldUpdateAndReturnProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse result = projectService.update(1L, projectDTO);

        assertNotNull(result);
        assertEquals("Project Alpha", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void delete_shouldDeleteProject() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        projectService.delete(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }
}
