package com.cos.security1.config.auth.oauth.provider;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        ObjectMapper mapper = new ObjectMapper();
        Object response = attributes.get("response");
        this.attributes = mapper.convertValue(response, Map.class);

    }

    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }
}
