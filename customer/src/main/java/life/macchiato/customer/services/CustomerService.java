package life.macchiato.customer.services;

import life.macchiato.customer.dto.CustomerRegistrationRequest;
import life.macchiato.customer.models.Customer;
import life.macchiato.customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepo;

    public void registerCustomer(CustomerRegistrationRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .build();
        customerRepo.save(customer);
    }
}
