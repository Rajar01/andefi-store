package store.andefi.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.andefi.core.enums.Role;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private UUID id;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private Instant verifiedAt;
    private Role role;
    private ShippingAddress shippingAddress;
}
