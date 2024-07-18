package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

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
}
