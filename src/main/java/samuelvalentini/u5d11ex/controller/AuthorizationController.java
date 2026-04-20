package samuelvalentini.u5d11ex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelvalentini.u5d11ex.dto.EmployeeDTO;
import samuelvalentini.u5d11ex.dto.LoginRequestDTO;
import samuelvalentini.u5d11ex.dto.LoginResponseDTO;
import samuelvalentini.u5d11ex.dto.NewEmployeeResponseDTO;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.service.AuthorizationService;
import samuelvalentini.u5d11ex.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private final EmployeeService employeeService;

    public AuthorizationController(AuthorizationService authorizationService, EmployeeService employeeService) {
        this.authorizationService = authorizationService;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return new LoginResponseDTO(this.authorizationService.checkCredentialsAndGenerateToken(loginRequestDTO));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeResponseDTO saveEmployee(@RequestBody @Validated EmployeeDTO employeeDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new BadRequestException(errors);
        }
        Employee newEmployee = this.employeeService.saveEmployee(employeeDTO);
        return new NewEmployeeResponseDTO(newEmployee.getEmployeeId());
    }
}
