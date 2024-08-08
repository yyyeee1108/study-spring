package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// 수동 구성한 빈 설정 클래스는 빼둔다
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
    // 기존과 다르게 @Bean으로 등록한 클래스가 없다

    // 수동으로 만든 경우 -> 자동으로 만들어진 빈 오버라이드 => 이런 애매한 상황 피하자
    // 스프링부트에서는 수동-자동 빈 충돌 시 오류 나도록 해둠
    /*@Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }*/
}
