package com.hit.joonggonara.repository.chat.querydsl;

import com.hit.joonggonara.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomQueryDsl {
    List<ChatRoom> findAllByNickName(String nickName);
    Optional<ChatRoom> findChatInChatRoomAllByRoomId(Long roomId);
    Optional<ChatRoom> findChatRoomByBuyerNickNameAndSellerNickNameAndProductId(String buyerNickName, String sellerNickName, Long productId);
    Optional <ChatRoom> findByRoomId(Long roomId);
}
