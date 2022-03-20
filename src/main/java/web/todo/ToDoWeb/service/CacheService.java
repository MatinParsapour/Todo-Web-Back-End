package web.todo.ToDoWeb.service;

public interface CacheService {

    void addEmailCode(String email, String code);

    void addPhoneNumberCode(String phoneNumber, Integer code);

    String getEmailCode(String email);

    Integer getPhoneNumberCode(String phoneNumber);
}
