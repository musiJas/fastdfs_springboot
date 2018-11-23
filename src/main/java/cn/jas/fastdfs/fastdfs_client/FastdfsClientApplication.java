package cn.jas.fastdfs.fastdfs_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = FastdfsClientApplication.class)
@SpringBootApplication
public class FastdfsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastdfsClientApplication.class, args);
    }
}
