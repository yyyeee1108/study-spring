package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy(); // ver1. 처음 기획에 따라 고정 금액 정책으로 개발
//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); // ver2. 바뀐 기획에 따라 정률 정책으로 개발. DIP, OCP 위반
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy; // DIP 가능하게 한다 (인터페이스에만 의존)

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {

        Member member = memberRepository.findById(memberId);
        /*
         * OrderService 입장에서 DiscountPolicy 자세한 건 몰라야 한다
         * 알아서 해달라고 하고 결과만 받으면 됨
         * Single Responsibility Principle (단일 책임 원칙)을 잘 지킨 케이스

         * 확장성을 고려하여 discount에는 member를 통째로 보낸다
         * 사실 member grade만 보내도 된다
         */
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
