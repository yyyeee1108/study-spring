package hello.core.singleton;

public class SingletonService {

    // static -> 클래스 레벨에 올라감 => 딱 한 개만 존재하게 된다
    private static final SingletonService instance = new SingletonService();

    // SingletonService를 꺼낼 수 있는 유일한 방법
    public static SingletonService getInstance() {
        return instance;
    }

    // private 생성자로 외부 생성을 막는다
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
