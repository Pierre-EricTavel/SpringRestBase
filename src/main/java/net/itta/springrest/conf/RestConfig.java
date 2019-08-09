package net.itta.springrest.conf;


import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
@EnableAsync
@ComponentScan( basePackages = {"net.itta"})
public class RestConfig {
    
    @Bean
    public View jsonTemplate(){
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrettyPrint(true);
         return view;
    }
    @Bean
    public ViewResolver viewResolver (){
        ViewResolver viewResolver = new BeanNameViewResolver();
        return viewResolver;
    }
}
