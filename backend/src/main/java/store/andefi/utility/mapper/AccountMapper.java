package store.andefi.utility.mapper;

import java.util.stream.Collectors;
import store.andefi.dto.AccountDto;
import store.andefi.entity.Account;
import store.andefi.entity.Role;

public final class AccountMapper {
  public static AccountDto toDto(Account accountEntity) {
    AccountDto accountDto = new AccountDto();
    accountDto.setId(accountEntity.getId().toString());
    accountDto.setEmail(accountEntity.getEmail());
    accountDto.setFullName(accountEntity.getFullName());
    accountDto.setUsername(accountEntity.getUsername());
    accountDto.setPhoneNumber(accountEntity.getPhoneNumber());
    accountDto.setShippingAddress(ShippingAddressMapper.toDto(accountEntity.getShippingAddress()));
    accountDto.setRoles(
        accountEntity.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    accountDto.setVerified(accountEntity.isVerified());
    accountDto.setVerifiedAt(accountEntity.getVerifiedAt());

    return accountDto;
  }
}
