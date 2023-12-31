package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.weather.ApiController;
import com.project.awesomegroup.dto.weather.Region;
import com.project.awesomegroup.repository.ApiReepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiReepository apiRepository;

    public void insert(Region region){apiRepository.save(region);}

}
