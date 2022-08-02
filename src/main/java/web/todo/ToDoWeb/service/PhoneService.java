package web.todo.ToDoWeb.service;

import web.todo.ToDoWeb.model.User;

public interface PhoneService extends BaseService<User, String> {

    void validatePhoneNumberAndUsername(Long phoneNumber, String userId);

    void processCode(Long phoneNumber, String username);

    void sendSMS(String phoneNumber, String message);

    Boolean isCodeValid(int code);

    void updateUser();

    void resendCode();

    void clear();

    void notEmptyAssertion(String attribute);

    Boolean resendCodeOrUpdateUser(int code);
}
