package net.itta.springrest.conf;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;


@Configuration
public class MyAsyncConfig {
    
    @Bean(name="asyncExecutor")
    public Executor asyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(3);
        executor.setThreadNamePrefix("executor-");
        executor.initialize();
        return executor;
    }
    
    
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
    

