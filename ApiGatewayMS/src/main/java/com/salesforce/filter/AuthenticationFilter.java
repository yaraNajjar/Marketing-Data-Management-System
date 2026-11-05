package com.salesforce.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.salesforce.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;

	// @Autowired
//    private RestTemplate template;
	@Autowired
	private JwtUtil jwtUtil;

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header");
				}

				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				try {
//                    //REST call to AUTH service
					jwtUtil.validateToken(authHeader);

					//
					String path = exchange.getRequest().getPath().value();
					String requiredRole = config.getRequiredRole(path);
					String userRole = config.getUserRole(exchange.getRequest());

					if (userRole != null && userRole.equals(requiredRole)) {
						return chain.filter(exchange);
					} else {
						exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
						return exchange.getResponse().setComplete();
					}

				} catch (Exception e) {
					System.out.println("invalid access...!");
					throw new RuntimeException("un authorized access to application");
				}
			}
			return chain.filter(exchange);
		});
	}

	public static class Config {
		// Implement methods to get user role and required role based on the request
		// path
		public String getUserRole(ServerHttpRequest request) {
			// TODO: Implement logic to extract user role from the request
			return "USER";

		}

		public String getRequiredRole(String path) {
			// TODO:Implement logic to determine required role for the requested path
			if (path.startsWith("/coupons")) {
				return "USER";
			} else {
				return "ADMIN";
			}
		}
	}
}
