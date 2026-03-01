package com.employeemanagement.service;

import com.employeemanagement.dto.request.EmployeeProjectRequest;
import com.employeemanagement.dto.response.EmployeeProjectResponse;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.EmployeeProject;
import com.employeemanagement.entity.Project;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.repository.EmployeeProjectRepository;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeProjectService {

    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<EmployeeProjectResponse> findAll() {
        return employeeProjectRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeProjectResponse findById(Long id) {
        EmployeeProject ep = employeeProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeProject", id));
        return toResponse(ep);
    }

    @Transactional
    public EmployeeProjectResponse create(EmployeeProjectRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", request.getEmployeeId()));
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", request.getProjectId()));

        EmployeeProject ep = EmployeeProject.builder()
                .employee(employee)
                .project(project)
                .assignedDate(request.getAssignedDate() != null ? request.getAssignedDate() : java.time.LocalDate.now())
                .role(request.getRole())
                .build();
        return toResponse(employeeProjectRepository.save(ep));
    }

    @Transactional
    public EmployeeProjectResponse update(Long id, EmployeeProjectRequest request) {
        EmployeeProject ep = employeeProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeProject", id));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", request.getEmployeeId()));
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", request.getProjectId()));

        ep.setEmployee(employee);
        ep.setProject(project);
        ep.setAssignedDate(request.getAssignedDate() != null ? request.getAssignedDate() : ep.getAssignedDate());
        ep.setRole(request.getRole());

        return toResponse(employeeProjectRepository.save(ep));
    }

    @Transactional
    public void delete(Long id) {
        if (!employeeProjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("EmployeeProject", id);
        }
        employeeProjectRepository.deleteById(id);
    }

    private EmployeeProjectResponse toResponse(EmployeeProject ep) {
        return EmployeeProjectResponse.builder()
                .id(ep.getId())
                .employeeId(ep.getEmployee().getId())
                .employeeName(ep.getEmployee().getName())
                .projectId(ep.getProject().getId())
                .projectName(ep.getProject().getName())
                .assignedDate(ep.getAssignedDate())
                .role(ep.getRole())
                .build();
    }
}
