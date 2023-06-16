package xyz.xminao.yelplet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("xyz.xminao.yelplet.mapper")
@SpringBootApplication
public class YelpletApplication {

    public static void main(String[] args) {
        SpringApplication.run(YelpletApplication.class, args);
    }

}
