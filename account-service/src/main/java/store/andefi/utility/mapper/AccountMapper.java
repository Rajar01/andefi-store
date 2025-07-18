package store.andefi.utility.mapper;

import store.andefi.dto.AccountDto;
import store.andefi.entity.Account;

public final class AccountMapper {
  public static AccountDto toDto(Account account) {
    return new AccountDto(
        account.id().toString(),
        account.email(),
        account.username(),
        null,
        account.isVerified(),
        account.verifiedAt());
  }
}
