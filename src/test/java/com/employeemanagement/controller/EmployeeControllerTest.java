package com.employeemanagement.controller;

import com.employeemanagement.dto.request.EmployeeRequest;
import com.employeemanagement.dto.response.EmployeeResponse;
import com.employeemanagement.exception.GlobalExceptionHandler;
import com.employeemanagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getAllEmployees_shouldReturn200() throws Exception {
        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john@company.com")
                .salary(BigDecimal.valueOf(75000))
                .departmentName("Engineering")
                .build();

        when(employeeService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john@company.com")));

        verify(employeeService, times(1)).findAll();
    }

    @Test
    void getEmployeeById_shouldReturn200() throws Exception {
        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john@company.com")
                .build();

        when(employeeService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(employeeService, times(1)).findById(1L);
    }

    @Test
    void createEmployee_shouldReturn201() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .name("Jane Smith")
                .email("jane@company.com")
                .salary(BigDecimal.valueOf(80000))
                .hireDate(LocalDate.of(2022, 3, 1))
                .departmentId(1L)
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
                .name("Jane Smith")
                .email("jane@company.com")
                .build();

        when(employeeService.create(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Jane Smith")));

        verify(employeeService, times(1)).create(any(EmployeeRequest.class));
    }

    @Test
    void updateEmployee_shouldReturn200() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .name("John Updated")
                .email("john@company.com")
                .salary(BigDecimal.valueOf(85000))
                .hireDate(LocalDate.of(2020, 1, 15))
                .departmentId(1L)
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
                .name("John Updated")
                .build();

        when(employeeService.update(eq(1L), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")));

        verify(employeeService, times(1)).update(eq(1L), any(EmployeeRequest.class));
    }

    @Test
    void deleteEmployee_shouldReturn204() throws Exception {
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).delete(1L);
    }
}
