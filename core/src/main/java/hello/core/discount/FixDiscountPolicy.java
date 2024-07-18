package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicy implements DiscountPolicy{

    private final int DISCOUNT_FIX_AMOUNT = 1000; // 1000원 할인

    @Override
    public int discount(Member member, int price) {

        // enum 타입은 == 을 사용하여 비교
        if (member.getGrade() == Grade.VIP) {
            return DISCOUNT_FIX_AMOUNT;
        } else {
            return 0;
        }
    }
}
