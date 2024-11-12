package com.example.demo.global.config.swagger;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Component
@Profile("!test && !prod")
public class ApiSuccessResponseHandler {

	private static final String APPLICATION_JSON = "application/json";
	private static final String IS_SUCCESS = "true";

	public void handleApiSuccessResponse(Operation operation, HandlerMethod handlerMethod) {
		// ApiResponseExplanations 어노테이션을 읽어옴
		ApiResponseExplanations apiResponseExplanations
			= handlerMethod.getMethodAnnotation(ApiResponseExplanations.class);

		// 어노테이션이 없다면 반환
		if (apiResponseExplanations == null) {
			return;
		}

		// 성공 응답 설명을 가져옴
		ApiSuccessResponseExplanation apiSuccessResponseExplanation = apiResponseExplanations.success();

		if (apiSuccessResponseExplanation != null) {
			ApiResponses responses = operation.getResponses();
			// 기본 200 OK 응답이 있다면 제거
			responses.remove("200");

			// 성공 response 구성
			io.swagger.v3.oas.models.media.Schema<?> responseSchema = new Schema<>()
				.addProperty("success",
					new Schema<>().type("string").example(IS_SUCCESS))
				.addProperty("data",
					apiSuccessResponseExplanation.responseClass()
						.isAssignableFrom(ApiSuccessResponseExplanation.EmptyClass.class)
						? new Schema<>().type("object")
						: new Schema<>().$ref(
						"#/components/schemas/" + apiSuccessResponseExplanation.responseClass().getSimpleName())
				);

			// ApiResponse를 만들어 operation의 응답에 추가
			ApiResponse apiResponse = new ApiResponse()
				.description(apiSuccessResponseExplanation.description())
				.content(new Content()
					.addMediaType(APPLICATION_JSON, new MediaType().schema(responseSchema))
				);

			// 성공 응답을 상태 코드에 맞게 추가 (예: 200)
			responses.addApiResponse(String.valueOf(apiSuccessResponseExplanation.status().value()), apiResponse);
		}
	}
}