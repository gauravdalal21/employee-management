package com.employeemanagement.service;

import com.employeemanagement.dto.request.DepartmentRequest;
import com.employeemanagement.dto.response.DepartmentResponse;
import com.employeemanagement.entity.Department;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.repository.DepartmentRepository;
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
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentRequest departmentRequest;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .name("Engineering")
                .location("New York")
                .build();

        departmentRequest = DepartmentRequest.builder()
                .name("Engineering")
                .location("New York")
                .build();
    }

    @Test
    void findAll_shouldReturnAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));

        List<DepartmentResponse> result = departmentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Engineering", result.get(0).getName());
        assertEquals("New York", result.get(0).getLocation());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void findById_whenDepartmentExists_shouldReturnDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentResponse result = departmentService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Engineering", result.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenDepartmentNotFound_shouldThrowResourceNotFoundException() {
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.findById(999L));
    }

    @Test
    void create_shouldSaveAndReturnDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentResponse result = departmentService.create(departmentRequest);

        assertNotNull(result);
        assertEquals("Engineering", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void update_shouldUpdateAndReturnDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentResponse result = departmentService.update(1L, departmentRequest);

        assertNotNull(result);
        assertEquals("Engineering", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void delete_shouldDeleteDepartment() {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.delete(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_whenDepartmentNotFound_shouldThrowResourceNotFoundException() {
        when(departmentRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> departmentService.delete(999L));
        verify(departmentRepository, never()).deleteById(anyLong());
    }
}
