package com.focusedapp.smartstudyhub.config.chatgpt;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class OpenAIConfig {

	@Value("${openai.api.key}")
	String openApiKeyEncoded;
	
	@Bean
	public RestTemplate restTemplate() {
		byte[] decodedBytes = Base64.getDecoder().decode(openApiKeyEncoded);
		String openApiKeyDecoded = new String(decodedBytes);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + openApiKeyDecoded);
			return execution.execute(request, body);
		});
		return restTemplate;
	}
}
