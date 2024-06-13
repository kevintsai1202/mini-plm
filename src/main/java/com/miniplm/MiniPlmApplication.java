package com.miniplm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.miniplm.repository.SystemSettingRepository;
import com.miniplm.service.FileStorageService;
import com.miniplm.service.RouteService;
import com.miniplm.service.SystemSettingService;


@SpringBootApplication
@EnableScheduling
//@EnableCaching
//@EnableJpaAuditing
public class MiniPlmApplication implements CommandLineRunner {

	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	SystemSettingService systemSettingService;
	
	@Autowired
	RouteService routeService;
	
	public static void main(String[] args) {
		SpringApplication.run(MiniPlmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Runner!!");
		fileStorageService.init();
		systemSettingService.init();
		routeService.init();
	}

}
