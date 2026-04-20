package samuelvalentini.u5d11ex.service;

import org.springframework.stereotype.Service;
import samuelvalentini.u5d11ex.dto.LoginRequestDTO;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.exception.NotFoundException;
import samuelvalentini.u5d11ex.exception.UnauthorizedException;
import samuelvalentini.u5d11ex.security.TokenTool;

@Service
public class AuthorizationService {
    private final EmployeeService employeeService;
    private final TokenTool tokenTool;

    public AuthorizationService(EmployeeService employeeService, TokenTool tokenTool) {
        this.employeeService = employeeService;
        this.tokenTool = tokenTool;


    }

    public String checkCredentialsAndGenerateToken(LoginRequestDTO loginRequestDTO) {
        try {
            Employee found = this.employeeService.findByEmail(loginRequestDTO.email());
            if (found.getPassword().equals(loginRequestDTO.password())) {
                return this.tokenTool.generateToken(found);
            } else {
                throw new UnauthorizedException("Incorrect credentials");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Incorrect credentials");
        }
    }
}
