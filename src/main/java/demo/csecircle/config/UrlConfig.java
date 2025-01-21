package demo.csecircle.config;


import demo.csecircle.interceptor.UrlFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UrlConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UrlFilter())
                .addPathPatterns("/**")
                .excludePathPatterns("/member/login",
                        "/member/register",
                        "/send",             // 이메일 인증 엔드포인트 추가
                        "/verify",           // 이메일 인증 엔드포인트 추가
                        "/css/**",          // 정적 리소스 경로
                        "/js/**",
                        "/images/**");

    }

}
