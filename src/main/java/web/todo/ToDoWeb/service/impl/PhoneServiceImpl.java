package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.CacheService;
import web.todo.ToDoWeb.service.CodeGeneratorService;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.PhoneService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class PhoneServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements PhoneService, FilledValidation {

    private final UserRepository userRepository;
    private final CodeGeneratorService codeGeneratorService;
    private final CacheService cacheService;
    private String userId;
    private Long phoneNumber;

    public PhoneServiceImpl(UserRepository repository, CodeGeneratorService codeGeneratorService, CacheService cacheCodeService) {
        super(repository);
        this.userRepository = repository;
        this.codeGeneratorService = codeGeneratorService;
        this.cacheService = cacheCodeService;
    }

    @Override
    public void validatePhoneNumberAndUsername(Long phoneNumber, String userId) {
        notEmptyAssertion(phoneNumber.toString());
        notEmptyAssertion(userId);
        processCode(phoneNumber, userId);
    }

    @Override
    public void processCode(Long phoneNumber, String userId) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        int code = codeGeneratorService.generateNumber();
        String message = "Hi, here's your code to validate your phone in todo app: " + code;
        cacheService.addPhoneNumberCode(phoneNumber.toString(), code);
        sendSMS(phoneNumber.toString(), message);
    }

    @Override
    public void sendSMS(String phoneNumber, String message) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://console.melipayamak.com/api/send/simple/a8894c4490974dad8b5995204ca86473");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            String params = "{\"from\": \"50004001753844\", \"to\": \" " +  phoneNumber + "\", \"text\": \" " + message + " \"}";
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            byte[] paramsAsByte = params.getBytes(StandardCharsets.UTF_8);
            dos.write(paramsAsByte, 0, paramsAsByte.length);
            dos.flush();
            dos.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String output = null;
            while (true) {
                try {
                    if ((output = br.readLine()) == null) break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                sb.append(output);
            }
        }
    }

    @Override
    public Boolean isCodeValid(int code) {
        Integer phoneNumberCode = cacheService.getPhoneNumberCode(phoneNumber.toString());
        if (!phoneNumberCode.equals(code)) {
            return false;
        }
        return true;
    }

    @Override
    public void updateUser() {
        User user = userRepository.findByUserNameAndIsDeletedFalse(userId).get();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    @Override
    public void resendCode() {
        processCode(phoneNumber, userId);
    }

    @Override
    public void clear() {
        userId = null;
        phoneNumber = null;
    }

    @Override
    public void notEmptyAssertion(String attribute) {
        if (isNull(attribute) || isBlank(attribute) || isWhiteSpace(attribute) || isEmpty(attribute)) {
            throw new EmptyException("The password provided is empty");
        }
    }

    @Override
    public Boolean resendCodeOrUpdateUser(int code) {
        Boolean isCodeValid = isCodeValid(code);
        if (!isCodeValid) {
            resendCode();
            return false;
        }
        updateUser();
        clear();
        return true;
    }

    @Override
    public Boolean isEmpty(String field) {
        return field.isEmpty();
    }

    @Override
    public Boolean isBlank(String field) {
        return field.isBlank();
    }

    @Override
    public Boolean isNull(String field) {
        return field == null;
    }

    @Override
    public Boolean isWhiteSpace(String field) {
        return field.trim().isEmpty();
    }
}
