package com.hit.joonggonara.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatRoom extends BaseEntity{

    // chatRoomId
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    private boolean buyerDeleted;
    private boolean sellerDeleted;


    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Chat> chats = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;



    @Builder
    public ChatRoom(Member buyer, Member seller, Product product) {
        this.buyer = buyer;
        this.seller = seller;
        this.buyerDeleted = false;
        this.sellerDeleted = false;
        addProduct(product);
    }

    public void addProduct(Product product) {
        if(this.product != null){
            this.product.getChatRooms().remove(this);
        }
        this.product = product;
        this.product.getChatRooms().add(this);
    }

    public void setBuyerDeleted(Boolean deleteStatus){
        this.buyerDeleted = deleteStatus;
    }
    public void setSellerDeleted(Boolean deleteStatus){
        this.sellerDeleted = deleteStatus;
    }
}
