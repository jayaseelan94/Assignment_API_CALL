package net.javaguides.springboot.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	 @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public void saveEmployee(Employee employee) {
		this.employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		Employee employee = null;
		if (optional.isPresent()) {
			employee = optional.get();
		} else {
			throw new RuntimeException(" Employee not found for id :: " + id);
		}
		return employee;
	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
	}

	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.employeeRepository.findAll(pageable);
	}
	
	private HttpHeaders gethttpHeaders(){
	       HttpHeaders headers = new HttpHeaders();
	       headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	       headers.setContentType(MediaType.APPLICATION_JSON);
	       return headers;
	    }
	
	 String baseUrl = "https://jsonplaceholder.typicode.com/";

	    StringBuilder stringBuilder  = new StringBuilder(baseUrl);

	    String POST  = "/posts";

	    String POSTBYID  = "/posts/";

	   
	   
	    @Override
	    public List<Map<String, Object>> getPosts() {
	        HttpEntity <Void> httpEntity  = new HttpEntity<>(gethttpHeaders());
	       String url = stringBuilder.append(POST).toString();
	          ResponseEntity<List> response = restTemplate().exchange(url , HttpMethod.GET ,httpEntity,List.class); 
	        return  response.getBody();
	    }
}
