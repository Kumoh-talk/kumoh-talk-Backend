package com.example.demo.global.config.swagger;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("!test && !prod")
@RequiredArgsConstructor
public class SwaggerConfig {
	private static final String JWT = "JWT";
	private final Environment environment;
	private final ApiSuccessResponseHandler apiSuccessResponseHandler;
	private final ApiErrorResponseHandler apiErrorResponseHandler;

	@Bean
	public OpenAPI openApi() {
		String activeProfile = "";
		if (!ObjectUtils.isEmpty(environment.getActiveProfiles()) && environment.getActiveProfiles().length >= 1) {
			activeProfile = environment.getActiveProfiles()[0];
		}

		String serverUrl = environment.getProperty("swagger.server-url");
		String serverDescription = environment.getProperty("swagger.description");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);
		return new OpenAPI()
			.info(apiInfo(activeProfile))
			.addServersItem(
				new io.swagger.v3.oas.models.servers.Server()
					.url(serverUrl)
					.description(serverDescription)
			)
			.addSecurityItem(securityRequirement)
			.components(securitySchemes());
	}

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}

	@Bean
	public OperationCustomizer customize() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			apiSuccessResponseHandler.handleApiSuccessResponse(operation, handlerMethod);
			apiErrorResponseHandler.handleApiErrorResponse(operation, handlerMethod);
			return operation;
		};
	}

	private Components securitySchemes() {
		final SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
			.name(JWT)
			.type(SecurityScheme.Type.HTTP)
			.scheme("Bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		return new Components()
			.addSecuritySchemes(JWT, accessTokenSecurityScheme);
	}

	private Info apiInfo(String activeProfile) {
		return new Info()
			.title("백엔드 API 명세서 (" + activeProfile + ")")
			.description(
				"<p>이 문서는 KumohTalk 백엔드 API의 사용 방법과 예시를 제공합니다. "
					+
					"API에 대한 보다 자세한 설명은 <a href=\"https://kyxxn.notion.site/39d4878d23dd439da70c7c4dc33a8a1c?pvs=4\" target='_blank'>API 개요</a>를 참고해 주세요.</p>"
					+
					"<p>API 사용 중 문제가 발생하거나 추가 문의가 필요한 경우, 담당자에게 문의해 주세요.</p>"
					+
					"<p>API 명세의 예외 상황은 도메인에 대한 에러가 명시되어 있습니다. field Validation 에러는 명시되어 있지않고 4XX 에러로 반환하게 됩니다</p>"
					+
					"<p>5XX 에러는 정의되지 않은 서버 에러이므로 해당 담당자 혹은 디스코드로 문의해 주세요</p>"
			)
			.version("v1.0.0");
	}
}
