package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.EmptyException;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.repository.UserRepository;
import web.todo.ToDoWeb.service.FilledValidation;
import web.todo.ToDoWeb.service.PhoneService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PhoneServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements PhoneService, FilledValidation {

    private final UserRepository userRepository;
    private String username;
    private Long phoneNumber;
    private Integer code;

    public PhoneServiceImpl(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    public void validatePhoneNumberAndUsername(Long phoneNumber, String username) {
        notEmptyAssertion(phoneNumber.toString());
        notEmptyAssertion(username);
        processCode(phoneNumber, username);
    }

    @Override
    public void processCode(Long phoneNumber, String username) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        int code = ThreadLocalRandom.current().nextInt(10000,99999);
        this.code = code;
        String message = "Hi, here's your code to validate your phone in todo app: " + code;
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
            byte[] paramsAsByte = params.getBytes("utf-8");
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
    public Boolean validateCode(int code) {
        if (this.code == code){
            updateUser();
            clear();
            return true;
        }
        resendCode();
        return false;
    }

    @Override
    public void updateUser() {
        User user = userRepository.findByUserName(username).get();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    @Override
    public void resendCode() {
        processCode(phoneNumber, username);
    }

    @Override
    public void clear() {
        username = null;
        phoneNumber = null;
        code = null;
    }

    @Override
    public void notEmptyAssertion(String attribute){
        if (isNull(attribute) || isBlank(attribute) || isWhiteSpace(attribute) || isEmpty(attribute)) {
            throw new EmptyException("The password provided is empty");
        }
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
