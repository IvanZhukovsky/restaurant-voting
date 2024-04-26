package ru.asphaltica.restaurantvoting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
@EnableCaching
public class AppConfig {
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public Server h2Server() throws SQLException {
//        log.info("Start H2 TCP server");
//        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    //}

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {return new ObjectMapper();}

}
