package com.focusedapp.smartstudyhub.config.scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean(destroyMethod="shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(100);
	}
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

	  ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
	  threadPoolTaskScheduler.setPoolSize(5);
	  threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
	  return threadPoolTaskScheduler;
	}
}
