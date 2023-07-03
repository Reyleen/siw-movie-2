package it.uniroma3.siw.controller.validator;


import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CredentialValidator implements Validator{

    @Autowired
    private CredentialValidator credentialValidator;

    @Autowired
    private CredentialsRepository credentialRepo;

    @Override
    public void validate(Object o, Errors errors) {
        Credentials c= (Credentials)o;
        if (c.getUsername()!=null && this.credentialRepo.existsByUsername(c.getUsername())){
            errors.reject("credentials.duplicate");
        }
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return Credentials.class.equals(aClass);
    }
}
