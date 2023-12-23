package com.project.awesomegroup.controller.wather;

import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.wather.Region;
import com.project.awesomegroup.service.ApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin("*")
@RequestMapping("/weather-api-init")
@PropertySource("classpath:application.yml")
@Tag(name = "Weather")
public class ApiController  {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    ApiService apiService;

    @Value("${resources.location}")
    private String resourceLocation;

    private final EntityManager em;

    public ApiController(EntityManager em) {
        this.em = em;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> restRegionList(){
        String fileLocation = resourceLocation + "/weather_init.csv";
        Path path = Paths.get(fileLocation);
        URI uri = path.toUri();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new UrlResource(uri).getInputStream()))
        ) {
            String line = br.readLine(); // head 떼기
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                logger.info(line);
                String[] splits = line.split(",");
                em.persist(new Region(Long.parseLong(splits[0]), splits[1], splits[2],
                        Integer.parseInt(splits[3]), Integer.parseInt(splits[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("초기화에 성공했습니다");
    }
}
