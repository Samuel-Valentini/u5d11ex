package samuelvalentini.u5d11ex.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samuelvalentini.u5d11ex.dto.EmployeeDTO;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public Employee saveEmployee(@RequestBody @Valid EmployeeDTO employeeDTO,
                                 BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        return employeeService.saveEmployee(employeeDTO);
    }

    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.findById(employeeId);
    }

    @GetMapping("/me")
    public Employee getOwnProfile(@AuthenticationPrincipal Employee currentAuthenticatedEmployee) {
        return currentAuthenticatedEmployee;
    }

    @PutMapping("/me")
    public Employee updateOwnProfile(@AuthenticationPrincipal Employee currentAuthenticatedEmployee, @RequestBody @Valid EmployeeDTO employeeDTO, BindingResult validationResult) {
        return updateEmployee(currentAuthenticatedEmployee.getEmployeeId(), employeeDTO, validationResult);

    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnProfile(@AuthenticationPrincipal Employee currentAuthenticatedEmployee) {
        deleteEmployee(currentAuthenticatedEmployee.getEmployeeId());
    }


    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Employee updateEmployee(@PathVariable Long employeeId,
                                   @RequestBody @Valid EmployeeDTO employeeDTO,
                                   BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        return employeeService.updateEmployee(employeeId, employeeDTO);
    }

    @PatchMapping("/{employeeId}/profile-picture")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Employee updateProfilePicture(@PathVariable Long employeeId,
                                         @RequestParam("profilePicture") MultipartFile file) {
        return employeeService.updateProfilePicture(employeeId, file);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
    }
}