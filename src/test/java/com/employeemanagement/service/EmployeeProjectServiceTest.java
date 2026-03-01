package com.employeemanagement.service;

import com.employeemanagement.dto.request.EmployeeProjectRequest;
import com.employeemanagement.dto.response.EmployeeProjectResponse;
import com.employeemanagement.entity.Department;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.EmployeeProject;
import com.employeemanagement.entity.Project;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.repository.EmployeeProjectRepository;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeProjectServiceTest {

    @Mock
    private EmployeeProjectRepository employeeProjectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private EmployeeProjectService employeeProjectService;

    private Department department;
    private Employee employee;
    private Project project;
    private EmployeeProject employeeProject;
    private EmployeeProjectRequest request;

    @BeforeEach
    void setUp() {
        department = Department.builder().id(1L).name("Engineering").build();
        employee = Employee.builder().id(1L).name("John Doe").department(department).build();
        project = Project.builder().id(1L).name("Project Alpha").department(department).build();

        employeeProject = EmployeeProject.builder()
                .id(1L)
                .employee(employee)
                .project(project)
                .assignedDate(LocalDate.now())
                .role("Developer")
                .build();

        request = EmployeeProjectRequest.builder()
                .employeeId(1L)
                .projectId(1L)
                .assignedDate(LocalDate.now())
                .role("Developer")
                .build();
    }

    @Test
    void findAll_shouldReturnAllAssignments() {
        when(employeeProjectRepository.findAll()).thenReturn(List.of(employeeProject));

        List<EmployeeProjectResponse> result = employeeProjectService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getEmployeeName());
        assertEquals("Project Alpha", result.get(0).getProjectName());
        verify(employeeProjectRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnAssignment() {
        when(employeeProjectRepository.findById(1L)).thenReturn(Optional.of(employeeProject));

        EmployeeProjectResponse result = employeeProjectService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Developer", result.getRole());
        verify(employeeProjectRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotFound_shouldThrowResourceNotFoundException() {
        when(employeeProjectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeProjectService.findById(999L));
    }

    @Test
    void create_shouldSaveAndReturnAssignment() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeProjectRepository.save(any(EmployeeProject.class))).thenReturn(employeeProject);

        EmployeeProjectResponse result = employeeProjectService.create(request);

        assertNotNull(result);
        assertEquals("Developer", result.getRole());
        verify(employeeProjectRepository, times(1)).save(any(EmployeeProject.class));
    }

    @Test
    void delete_shouldDeleteAssignment() {
        when(employeeProjectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeProjectRepository).deleteById(1L);

        employeeProjectService.delete(1L);

        verify(employeeProjectRepository, times(1)).deleteById(1L);
    }
}
