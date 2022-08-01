package web.todo.ToDoWeb.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.VerificationCodeTimeOutException;
import web.todo.ToDoWeb.service.CacheService;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class CacheServiceImpl implements CacheService {
    private static final int ATTEMPT_INCREMENT = 1;
    private static final Integer MAXIMUM_NUMBER_OF_ATTEMPT = 5;
    private final LoadingCache<String, String> loadingCacheForEmail;
    private final LoadingCache<String, Integer> loadingCacheForPhoneNumber;
    private final LoadingCache<String, Integer> loadingCacheForLoginAttempts;

    public CacheServiceImpl() {
        super();
        this.loadingCacheForEmail = CacheBuilder.newBuilder()
                .expireAfterWrite(2, MINUTES).
                        build(new CacheLoader<String, String>() {
                            @Override
                            public String load(String key) throws Exception {
                                return "";
                            }
                        });
        this.loadingCacheForPhoneNumber = CacheBuilder.newBuilder()
                .expireAfterWrite(2, MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
        this.loadingCacheForLoginAttempts = CacheBuilder.newBuilder()
                .expireAfterWrite(15, MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }

    @Override
    public void addEmailCode(String email, String code) {
        loadingCacheForEmail.put(email, code);
    }

    @Override
    public void addPhoneNumberCode(String phoneNumber, Integer code) {
        loadingCacheForPhoneNumber.put(phoneNumber, code);
    }

    @Override
    public String getEmailCode(String email) {
        String code = loadingCacheForEmail.getIfPresent(email);
        if (code != null) {
            return code;
        }
        throw new VerificationCodeTimeOutException("This code is expired");
    }

    @Override
    public Integer getPhoneNumberCode(String phoneNumber) {
        Integer code = loadingCacheForPhoneNumber.getIfPresent(phoneNumber);
        if (code != null) {
            return code;
        }
        throw new VerificationCodeTimeOutException("This code is expired");
    }

    @Override
    public void addUserLoginAttempt(String userId) {
        int attempts = 0;
        try {
            attempts += ATTEMPT_INCREMENT + loadingCacheForLoginAttempts.get(userId);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        loadingCacheForLoginAttempts.put(userId, attempts);
    }


    @Override
    public boolean hasExceededMaxAttempts(String username) {
        try {
            return loadingCacheForLoginAttempts.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPT;
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeUserLoginAttemptsFromCache(String userId) {
        loadingCacheForLoginAttempts.invalidate(userId);
    }
}
