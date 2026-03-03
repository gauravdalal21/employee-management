package com.employeemanagement.controller;

import com.employeemanagement.dto.request.DepartmentRequest;
import com.employeemanagement.dto.response.DepartmentResponse;
import com.employeemanagement.exception.GlobalExceptionHandler;
import com.employeemanagement.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
@Import(GlobalExceptionHandler.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    @Test
    void getAllDepartments_shouldReturn200() throws Exception {
        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Engineering")
                .location("Building A")
                .build();

        when(departmentService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Engineering")))
                .andExpect(jsonPath("$[0].location", is("Building A")));

        verify(departmentService, times(1)).findAll();
    }

    @Test
    void getDepartmentById_shouldReturn200() throws Exception {
        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Engineering")
                .location("Building A")
                .build();

        when(departmentService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Engineering")));

        verify(departmentService, times(1)).findById(1L);
    }

    @Test
    void createDepartment_shouldReturn201() throws Exception {
        DepartmentRequest request = DepartmentRequest.builder()
                .name("HR")
                .location("Building B")
                .build();

        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("HR")
                .location("Building B")
                .build();

        when(departmentService.create(any(DepartmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("HR")));

        verify(departmentService, times(1)).create(any(DepartmentRequest.class));
    }

    @Test
    void updateDepartment_shouldReturn200() throws Exception {
        DepartmentRequest request = DepartmentRequest.builder()
                .name("Engineering Updated")
                .location("Building C")
                .build();

        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Engineering Updated")
                .build();

        when(departmentService.update(eq(1L), any(DepartmentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Engineering Updated")));

        verify(departmentService, times(1)).update(eq(1L), any(DepartmentRequest.class));
    }

    @Test
    void deleteDepartment_shouldReturn204() throws Exception {
        doNothing().when(departmentService).delete(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).delete(1L);
    }
}
