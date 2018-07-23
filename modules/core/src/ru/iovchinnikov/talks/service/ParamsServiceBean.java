package ru.iovchinnikov.talks.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service(ParamsService.NAME)
public class ParamsServiceBean implements ParamsService {

    private Map<String,Object> params;

    @Override
    public void setParams(Map<String, Object> params) {
        this.params=params;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }
}