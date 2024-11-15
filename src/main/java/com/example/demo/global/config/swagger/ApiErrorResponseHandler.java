package com.example.demo.global.config.swagger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.example.demo.global.base.dto.FailedResponseBody;
import com.example.demo.global.base.exception.ErrorCode;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.Builder;
import lombok.Getter;

@Component
@Profile("!test && !prod")
public class ApiErrorResponseHandler {

	public void handleApiErrorResponse(Operation operation, HandlerMethod handlerMethod) {
		ApiResponseExplanations apiResponseExplanations
			= handlerMethod.getMethodAnnotation(ApiResponseExplanations.class);

		if (apiResponseExplanations != null) {
			generateResponseCodeResponseExample(operation, Arrays.asList(apiResponseExplanations.errors()));
		}
	}

	private void generateResponseCodeResponseExample(Operation operation,
		List<ApiErrorResponseExplanation> apiErrorResponseExamples) {
		ApiResponses responses = operation.getResponses();

		Map<Integer, List<ExampleHolder>> statusWithExampleHolders = apiErrorResponseExamples.stream()
			.map(this::createExampleHolder)
			.collect(Collectors.groupingBy(ExampleHolder::getHttpStatusCode));

		addExamplesToResponses(responses, statusWithExampleHolders);
	}

	private ExampleHolder createExampleHolder(ApiErrorResponseExplanation apiErrorResponseExample) {
		ErrorCode errorCode = apiErrorResponseExample.errorCode();
		return ExampleHolder.builder()
			.httpStatusCode(errorCode.getStatus().value()) // HTTP 상태 코드
			.name(errorCode.name()) // enum 이름
			.errorCode(errorCode.getCode()) // 커스텀 에러 코드
			.description(errorCode.getMessage()) // 에러 메시지
			.holder(createSwaggerExample(errorCode, errorCode.getMessage()))
			.build();
	}

	private Example createSwaggerExample(ErrorCode errorCode, String description) {
		FailedResponseBody failedResponseBody
			= new FailedResponseBody(errorCode.getCode(), errorCode.getMessage());
		ExampleFailedResponseBody failedResponseBodyExample = new ExampleFailedResponseBody("false", failedResponseBody);

		Example example = new Example();
		example.setValue(failedResponseBodyExample);
		example.setDescription(description); // 설명을 예제에 추가

		return example;
	}

	private void addExamplesToResponses(
		ApiResponses responses,
		Map<Integer, List<ExampleHolder>> statusWithExampleHolders
	) {
		statusWithExampleHolders.forEach((status, exampleHolders) -> {
			Content content = new Content();
			MediaType mediaType = new MediaType();
			ApiResponse apiResponse = new ApiResponse();

			exampleHolders.forEach(
				exampleHolder -> mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder())
			);

			content.addMediaType("application/json", mediaType);
			apiResponse.setContent(content);
			responses.addApiResponse(String.valueOf(status), apiResponse);
		});
	}

	@Getter
	@Builder
	public static class ExampleHolder {
		private final int httpStatusCode;
		private final String name;
		private final String errorCode;
		private final String description;
		private final Example holder;
	}

	@Getter
	public static class ExampleFailedResponseBody {
		private  final String success;
		private  final String code;
		private  final String message;

		public ExampleFailedResponseBody(String success , FailedResponseBody failedResponseBody) {
			this.success = success;
			this.code = failedResponseBody.getCode();
			this.message = failedResponseBody.getMsg();
		}

	}

}
