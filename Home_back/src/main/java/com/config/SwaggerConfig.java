package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket makeDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2); // swagger 핵심 클래스
		ApiSelectorBuilder builder = docket.select(); // 문서 작성을 위한 내부 초기화 작업

		// 문서를 만들 대상 컨트롤러가 있는 패키지 지정
		builder = builder.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class));

		// api()로 선택된 api중 특정 path조건에 맞는 api를 다시 필터링하여 문서화 작업을 함
		builder = builder.paths(PathSelectors.ant("/**")); // path 명시를 ant방식으로
		docket = builder.build();

		docket = docket.apiInfo(apiInfo());
		return docket;
	}

	private ApiInfo apiInfo() {
		ApiInfoBuilder b = new ApiInfoBuilder();
		b = b.title("WhereIsMyHome API 입니다.");
		b = b.description(
				"<h3>WhereIsMyHome</h3>Swagger를 이용한 WhereIsMyHome API<br><br><img src=\"/resources/image/놀자에몽.jpg\" width=\"200\"> ");
		b.version("3.0");
		ApiInfo ai = b.build();
		return ai;
	}

}
