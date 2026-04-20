package samuelvalentini.u5d11ex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samuelvalentini.u5d11ex.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Employee> findByUsername(String username);

    boolean existsByUsernameAndEmployeeIdNot(String username, Long employeeId);

    boolean existsByEmailAndEmployeeIdNot(String email, Long employeeId);

}
