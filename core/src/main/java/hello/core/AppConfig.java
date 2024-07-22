package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Config에서 구현 객체를 생성, 주입
@Configuration // Spring으로 전환
public class AppConfig {

    @Bean // Spring으로 전환
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean // Spring으로 전환
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean // Spring으로 전환
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean // Spring으로 전환
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
