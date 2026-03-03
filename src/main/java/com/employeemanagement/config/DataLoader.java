package com.employeemanagement.config;

import com.employeemanagement.entity.Department;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.EmployeeProject;
import com.employeemanagement.entity.Project;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.repository.EmployeeProjectRepository;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository employeeProjectRepository;

    private static final String[] FIRST_NAMES = {"Gaurav", "Sukumar", "Srinivas", "Mohit", "Jaya", "Jennifer",
            "Jamila", "Poorva", "Sayma", "Rahul", "Dravid", "BalaKumar", "Yash", "Susan", "Jaykumar", "Jessica",
            "Praveen", "Sarah", "Charles", "Karen", "Christopher", "Nancy", "Daniel", "Lisa", "Matthew", "Betty",
            "Anthony", "Samson", "Surya", "Kumaran", "Donald", "Ashley", "Steven", "Kimberly", "Paul", "Emily"};

    private static final String[] LAST_NAMES = {"Vanasthali", "Sharma", "Kohli", "Shashtri", "Dhillon", "Singh",
            "Garcha", "Bumrah", "Sindhu", "Kharsa", "Bhaskaran", "Patel", "Patil", "Narsimhan", "Nagaraj",
            "Thomas", "Taylor", "Divekar", "Agarkar", "Tyagi", "Lee", "Walia", "Jain", "Agarwal", "Agnihotri"};

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void loadData() {
        if (employeeRepository.count() > 0) {
            log.info("Data already loaded, skipping initialization");
            return;
        }

        log.info("Loading initial data...");

        // Create departments
        List<Department> departments = createDepartments();
        departmentRepository.saveAll(departments);

        // Create projects
        List<Project> projects = createProjects(departments);
        projectRepository.saveAll(projects);

        // Create 55 employees
        List<Employee> employees = createEmployees(departments);
        employeeRepository.saveAll(employees);

        // Create employee-project assignments
        createEmployeeProjects(employees, projects);

        log.info("Data loaded successfully: {} departments, {} projects, {} employees",
                departments.size(), projects.size(), employees.size());
    }

    private List<Department> createDepartments() {
        return List.of(
                Department.builder().name("Engineering").location("New York").build(),
                Department.builder().name("Human Resources").location("San Francisco").build(),
                Department.builder().name("Finance").location("Hyderabad").build(),
                Department.builder().name("Marketing").location("Gurgaon").build(),
                Department.builder().name("Sales").location("Chennai").build(),
                Department.builder().name("Operations").location("Bengaluru").build()
        );
    }

    private List<Project> createProjects(List<Department> departments) {
        return List.of(
                Project.builder().name("Project Alpha").description("Core platform development").department(departments.get(0)).build(),
                Project.builder().name("Project Beta").description("Mobile app initiative").department(departments.get(0)).build(),
                Project.builder().name("Project Gamma").description("Cloud migration").department(departments.get(0)).build(),
                Project.builder().name("HR Portal").description("Employee self-service portal").department(departments.get(1)).build(),
                Project.builder().name("Recruitment Drive").description("Q4 hiring campaign").department(departments.get(1)).build(),
                Project.builder().name("Budget 2026").description("Annual budget planning").department(departments.get(2)).build(),
                Project.builder().name("Brand Refresh").description("Marketing rebrand").department(departments.get(3)).build(),
                Project.builder().name("Sales CRM").description("CRM implementation").department(departments.get(4)).build(),
                Project.builder().name("Supply Chain").description("Logistics optimization").department(departments.get(5)).build()
        );
    }

    private List<Employee> createEmployees(List<Department> departments) {
        List<Employee> employees = new java.util.ArrayList<>();
        int emailCounter = 1;

        for (int i = 0; i < 60; i++) {
            String firstName = FIRST_NAMES[i % FIRST_NAMES.length];
            String lastName = LAST_NAMES[i % LAST_NAMES.length];
            String name = firstName + " " + lastName;
            String email = "emp" + emailCounter + "." + lastName.toLowerCase() + "@company.com";
            emailCounter++;

            BigDecimal salary = BigDecimal.valueOf(45000 + ThreadLocalRandom.current().nextInt(100000));
            LocalDate hireDate = LocalDate.now().minusYears(ThreadLocalRandom.current().nextInt(1, 10))
                    .minusMonths(ThreadLocalRandom.current().nextInt(12));

            Department dept = departments.get(i % departments.size());

            employees.add(Employee.builder()
                    .name(name)
                    .email(email)
                    .salary(salary)
                    .hireDate(hireDate)
                    .department(dept)
                    .build());
        }
        return employees;
    }

    private void createEmployeeProjects(List<Employee> employees, List<Project> projects) {
        String[] roles = {"Lead", "Developer", "Analyst", "Designer", "Tester", "Manager"};
        for (int i = 0; i < 40; i++) {
            Employee emp = employees.get(i % employees.size());
            Project proj = projects.get(i % projects.size());

            EmployeeProject ep = EmployeeProject.builder()
                    .employee(emp)
                    .project(proj)
                    .assignedDate(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(1, 12)))
                    .role(roles[i % roles.length])
                    .build();
            employeeProjectRepository.save(ep);
        }
    }
}
