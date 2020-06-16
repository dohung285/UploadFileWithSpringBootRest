package com.ite.viettelpaygate.uploadfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ite.viettelpaygate.uploadfile.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class UploadfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadfileApplication.class, args);
	}

}
