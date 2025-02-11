package com.ginkgooai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GinkgooCoreMessagingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GinkgooCoreMessagingAppApplication.class, args);
    }

}
