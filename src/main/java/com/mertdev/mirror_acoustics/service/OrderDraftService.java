package com.mertdev.mirror_acoustics.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mertdev.mirror_acoustics.domain.Cart;
import com.mertdev.mirror_acoustics.domain.OrderDraft;
import com.mertdev.mirror_acoustics.repository.OrderDraftRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDraftService {
    private final OrderDraftRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderDraft saveDraft(Cart cart, String name, String phone, String email,
                                String address, String shipping, String note, String utm) {
        OrderDraft draft = new OrderDraft();
        try {
            draft.setCartSnapshot(mapper.writeValueAsString(cart));
        } catch (JsonProcessingException e) {
            draft.setCartSnapshot("{}");
        }
        draft.setName(name);
        draft.setPhone(phone);
        draft.setEmail(email);
        draft.setAddress(address);
        draft.setShippingPreference(shipping);
        draft.setNote(note);
        draft.setUtm(utm);
        return repository.save(draft);
    }
}
