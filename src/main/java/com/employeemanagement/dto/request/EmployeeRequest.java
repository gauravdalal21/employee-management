package com.employeemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @DecimalMin(value = "0", message = "Salary must be positive")
    private BigDecimal salary;

    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
