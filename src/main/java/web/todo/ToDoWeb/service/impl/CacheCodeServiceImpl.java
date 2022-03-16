package web.todo.ToDoWeb.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;
import web.todo.ToDoWeb.exception.VerificationCodeTimeOutException;
import web.todo.ToDoWeb.service.CacheCodeService;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class CacheCodeServiceImpl implements CacheCodeService {
    private final LoadingCache<String, String> loadingCacheForEmail;
    private final LoadingCache<String, Integer> loadingCacheForPhoneNumber;

    public CacheCodeServiceImpl() {
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
        if (code != null){
            return code;
        }
        throw new VerificationCodeTimeOutException("This code is expired");
    }

    @Override
    public Integer getPhoneNumberCode(String phoneNumber) {
        Integer code = loadingCacheForPhoneNumber.getIfPresent(phoneNumber);
        if (code != null){
            return code;
        }
        throw new VerificationCodeTimeOutException("This code is expired");
    }
}
