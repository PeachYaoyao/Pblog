package top.peachyao.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * @Description: 测试配置（跳过登录）
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Configuration
@Profile("test")   // 仅当激活 test profile 时生效
public class DisableSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/**"));
    }
}