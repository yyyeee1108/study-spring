package hello.core.singleton;

public class StatefulService {

    private int price; // 상태를 유지하는 필드

    // stateful하게 유지되는 price 필드 사용하는 메서드
    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제
    }

    // stateless하게 설계된 메서드
    public int orderStateless(String name, int price) {
        return price;
    }

    public int getPrice() {
        return price;
    }
}
