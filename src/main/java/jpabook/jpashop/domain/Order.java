package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column( name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order",  cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();



    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // ==연관관계 메소드 ==/
    public void setMember(Member member){
        this.member= member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    /*
    public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();

        member.getOrders().add(order);
        order.setMember(member);
    }
     */

    //==생성메소드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    /*
    주문취
     */

    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상풍은 취소가 불가능 합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: orderItems){
            orderItem.cancel();
        }
    }

    //전체 주문 가격 조회소
    /*
    전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice =0;
        for(OrderItem orderitem : orderItems ){
            totalPrice += orderitem.getTotalPrice();
        }
        return totalPrice;
    }

}
