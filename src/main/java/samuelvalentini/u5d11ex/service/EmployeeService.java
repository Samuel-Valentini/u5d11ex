package samuelvalentini.u5d11ex.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import samuelvalentini.u5d11ex.dto.EmployeeDTO;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.exception.NotFoundException;
import samuelvalentini.u5d11ex.repository.EmployeeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Cloudinary cloudinary;

    public EmployeeService(EmployeeRepository employeeRepository, Cloudinary cloudinary) {
        this.employeeRepository = employeeRepository;
        this.cloudinary = cloudinary;
    }

    public Employee saveEmployee(EmployeeDTO employeeDTO) {
        validateUniqueForCreate(employeeDTO);

        Employee employee = new Employee(
                employeeDTO.username().trim(),
                employeeDTO.password().trim(),
                employeeDTO.firstName().trim(),
                employeeDTO.lastName().trim(),
                employeeDTO.email().trim()
        );

        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException(String.valueOf(employeeId)));
    }

    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public Employee updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
        Employee found = findById(employeeId);

        validateUniqueForUpdate(employeeId, employeeDTO);

        found.setUsername(employeeDTO.username().trim());
        found.setFirstName(employeeDTO.firstName().trim());
        found.setLastName(employeeDTO.lastName().trim());
        found.setEmail(employeeDTO.email().trim());

        return employeeRepository.save(found);
    }

    public Employee updateProfilePicture(Long employeeId, MultipartFile file) {
        Employee found = findById(employeeId);
        if (file.isEmpty()) throw new BadRequestException("Void File");
        if (file.getSize() > 5_242_880)
            throw new BadRequestException("File too big, <5MB");
        if (file.getContentType() == null || !file.getContentType().startsWith("image/"))
            throw new BadRequestException("It has to be an image");
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BadRequestException("Invalid image file");
            }
        } catch (IOException e) {
            throw new BadRequestException("Corrupted file");
        }


        try {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            found.setProfilePicture(url);
            this.employeeRepository.save(found);
            return found;

        } catch (IOException e) {
            throw new BadRequestException("Error while uploading image");
        }

    }


    public void deleteEmployee(Long employeeId) {
        Employee found = findById(employeeId);
        employeeRepository.delete(found);
    }

    private void validateUniqueForCreate(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsername(employeeDTO.username().trim())) {
            throw new BadRequestException("Username already in use");
        }

        if (employeeRepository.existsByEmail(employeeDTO.email().trim())) {
            throw new BadRequestException("Email already in use");
        }
    }

    private void validateUniqueForUpdate(Long employeeId, EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsernameAndEmployeeIdNot(employeeDTO.username().trim(), employeeId)) {
            throw new BadRequestException("Username already in use");
        }

        if (employeeRepository.existsByEmailAndEmployeeIdNot(employeeDTO.email().trim(), employeeId)) {
            throw new BadRequestException("Email already in use");
        }
    }

}
