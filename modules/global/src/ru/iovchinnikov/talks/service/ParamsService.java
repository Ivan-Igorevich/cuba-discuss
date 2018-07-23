package ru.iovchinnikov.talks.service;


import java.util.Map;

public interface ParamsService {
    String NAME = "discuss_ParamsService";

    void setParams(Map<String,Object> params);

    Map<String,Object> getParams();
}