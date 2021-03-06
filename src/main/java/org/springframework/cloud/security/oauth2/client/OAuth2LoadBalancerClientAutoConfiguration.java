/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.security.oauth2.client;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * @author Dave Syer
 *
 */
@Configuration
@ConditionalOnClass({ LoadBalancerInterceptor.class, OAuth2RestTemplate.class })
@AutoConfigureBefore(OAuth2ClientAutoConfiguration.class)
public class OAuth2LoadBalancerClientAutoConfiguration {

	@Autowired(required = false)
	private LoadBalancerInterceptor loadBalancerInterceptor;

	@Autowired(required = false)
	private OAuth2RestTemplate restTemplate;

	@PostConstruct
	public void addInterceptor() {
		if (restTemplate != null && loadBalancerInterceptor != null) {
			List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>(
					restTemplate.getInterceptors());
			interceptors.add(loadBalancerInterceptor);
			restTemplate.setInterceptors(interceptors);
		}
	}

}
