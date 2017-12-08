package todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import todo.dao.MemoryTodosDao;
import todo.dao.TodosDao;

import javax.sql.DataSource;

@SpringBootApplication
@EnableAutoConfiguration
public class App {

    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public TodosDao memoryTodosDao() {
        return new MemoryTodosDao();
    }
}
