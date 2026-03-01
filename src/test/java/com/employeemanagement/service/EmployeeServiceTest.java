package com.employeemanagement.service;

import com.employeemanagement.dto.request.EmployeeRequest;
import com.employeemanagement.dto.response.EmployeeResponse;
import com.employeemanagement.entity.Department;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Department department;
    private Employee employee;
    private EmployeeRequest employeeRequest;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .name("Engineering")
                .location("New York")
                .build();

        employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@company.com")
                .salary(BigDecimal.valueOf(75000))
                .hireDate(LocalDate.of(2020, 1, 15))
                .department(department)
                .build();

        employeeRequest = EmployeeRequest.builder()
                .name("John Doe")
                .email("john.doe@company.com")
                .salary(BigDecimal.valueOf(75000))
                .hireDate(LocalDate.of(2020, 1, 15))
                .departmentId(1L)
                .build();
    }

    @Test
    void findAll_shouldReturnAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeResponse> result = employeeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john.doe@company.com", result.get(0).getEmail());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void findById_whenEmployeeExists_shouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse result = employeeService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("Engineering", result.getDepartmentName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenEmployeeNotFound_shouldThrowResourceNotFoundException() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(999L));
        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    void create_shouldSaveAndReturnEmployee() {
        when(employeeRepository.existsByEmail(employeeRequest.getEmail())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse result = employeeService.create(employeeRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@company.com", result.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void create_whenEmailExists_shouldThrowException() {
        when(employeeRepository.existsByEmail(employeeRequest.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> employeeService.create(employeeRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void update_shouldUpdateAndReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(employeeRequest.getEmail())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse result = employeeService.update(1L, employeeRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void update_whenEmployeeNotFound_shouldThrowResourceNotFoundException() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.update(999L, employeeRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void delete_shouldDeleteEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_whenEmployeeNotFound_shouldThrowResourceNotFoundException() {
        when(employeeRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.delete(999L));
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
