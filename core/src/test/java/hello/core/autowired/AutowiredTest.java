package hello.core.autowired;

import hello.core.member.Member;
import io.micrometer.common.lang.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption() {
        // 스프링 빈으로 등록해줌
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    static class TestBean{
        // Member는 스프링 컨테이너에서 관리하는 빈이 아님 -> 즉 빈 아닌 것 넣었을 때

        // required false 하면 자동 주입할 대상이 없을 때 호출 자체가 안된다
        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }

        // @Nullable을 설정하면 자동 주입할 대상이 없을 때 null이 입력된다
        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);
        }

        // Optional을 설정해두면 자동 주입할 대상이 없을 때 Optional.empty가 입력된다
        @Autowired
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println("noBean3 = " + noBean3);
        }
    }
}
