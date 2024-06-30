package com.lec.spring.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class IamportService {

    @Value("${imp.client.code}")
    private String clientCode;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
        authenticate();
    }


    private void authenticate() {
        IamportResponse<AccessToken> authResponse = null;
        try {
            authResponse = iamportClient.getAuth();
        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (authResponse == null || authResponse.getResponse() == null) {
            throw new IllegalStateException("Iamport 인증에 실패했습니다.");
        }
    }

    public IamportClient getIamportClient() {
        return iamportClient;
    }
}


