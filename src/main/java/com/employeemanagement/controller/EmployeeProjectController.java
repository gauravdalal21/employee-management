package com.employeemanagement.controller;

import com.employeemanagement.dto.request.EmployeeProjectRequest;
import com.employeemanagement.dto.response.EmployeeProjectResponse;
import com.employeemanagement.service.EmployeeProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeProjectController {

    private final EmployeeProjectService employeeProjectService;

    @GetMapping
    public ResponseEntity<List<EmployeeProjectResponse>> getAllAssignments() {
        return ResponseEntity.ok(employeeProjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProjectResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeProjectService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeProjectResponse> createAssignment(@Valid @RequestBody EmployeeProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeProjectService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeProjectResponse> updateAssignment(@PathVariable Long id,
                                                                    @Valid @RequestBody EmployeeProjectRequest request) {
        return ResponseEntity.ok(employeeProjectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        employeeProjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
