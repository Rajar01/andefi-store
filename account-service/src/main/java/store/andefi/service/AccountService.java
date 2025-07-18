package store.andefi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import store.andefi.dto.AccountDto;
import store.andefi.dto.AccountRegisterDto;
import store.andefi.dto.AccountUpdateDto;
import store.andefi.dto.RoleDto;
import store.andefi.entity.Account;
import store.andefi.entity.Role;
import store.andefi.exception.DuplicationException;
import store.andefi.repository.AccountRepository;
import store.andefi.repository.RoleRepository;
import store.andefi.utility.mapper.AccountMapper;
import store.andefi.utility.mapper.RoleMapper;

@ApplicationScoped
public class AccountService {
  @Inject AccountRepository accountRepository;
  @Inject RoleRepository roleRepository;

  @Inject AuthenticationService authenticationService;

  public AccountDto getAccountById(String accountId) {
    Account accountEntity =
        accountRepository
            .findByIdOptional(new ObjectId(accountId))
            .orElseThrow(NotFoundException::new);

    List<RoleDto> roleDtos =
        accountEntity.roleIds().stream()
            .map(roleId -> roleRepository.findByIdOptional(new ObjectId(roleId)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(RoleMapper::toDto)
            .toList();

    AccountDto accountDto = AccountMapper.toDto(accountEntity);
    accountDto.setRoles(roleDtos);

    return accountDto;
  }

  public AccountDto getAccountByEmail(String accountEmail) {
    Account accountEntity =
        accountRepository
            .find("email", accountEmail)
            .firstResultOptional()
            .orElseThrow(NotFoundException::new);

    List<RoleDto> roleDtos =
        accountEntity.roleIds().stream()
            .map(roleId -> roleRepository.findByIdOptional(new ObjectId(roleId)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(RoleMapper::toDto)
            .toList();

    AccountDto accountDto = AccountMapper.toDto(accountEntity);
    accountDto.setRoles(roleDtos);

    return accountDto;
  }

  public void createAccount(AccountRegisterDto accountRegisterDto) throws JsonProcessingException {
    Predicate<String> isEmailAlreadyExist =
        email -> accountRepository.find("email", email).firstResultOptional().isPresent();

    Predicate<String> isUsernameAlreadyExist =
        username -> accountRepository.find("username", username).firstResultOptional().isPresent();

    // Check if email or username name already exist
    if (isEmailAlreadyExist.test(accountRegisterDto.email())
        || isUsernameAlreadyExist.test(accountRegisterDto.username())) {
      throw new DuplicationException();
    }

    // Get accountEntity role ids
    List<String> roleIds =
        accountRegisterDto.roleNames().stream()
            .map(roleName -> roleRepository.find("name", roleName).firstResultOptional())
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(Role::id)
            .map(ObjectId::toString)
            .toList();

    Account accountEntity =
        new Account(
            null,
            accountRegisterDto.email(),
            accountRegisterDto.username(),
            BCrypt.hashpw(accountRegisterDto.password(), BCrypt.gensalt()),
            roleIds,
            false,
            null,
            Instant.now(),
            Instant.now());

    accountRepository.persist(accountEntity);

    // Get newly created account entity
    accountEntity = accountRepository.find("email", accountEntity.email()).firstResult();

    List<RoleDto> roleDtos =
        accountEntity.roleIds().stream()
            .map(roleId -> roleRepository.findByIdOptional(new ObjectId(roleId)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(RoleMapper::toDto)
            .toList();

    AccountDto accountDto = AccountMapper.toDto(accountEntity);
    accountDto.setRoles(roleDtos);

    authenticationService.sendAccountVerificationEmail(accountDto);
  }

  public void updateAccount(AccountUpdateDto accountUpdateDto) {
    Account accountEntity =
        accountRepository
            .findByIdOptional(accountUpdateDto.id())
            .orElseThrow(NotFoundException::new);

    accountRepository.update(
        new Account(
            accountEntity.id(),
            accountUpdateDto.email().isPresent()
                ? accountUpdateDto.email().get()
                : accountEntity.email(),
            accountUpdateDto.username().isPresent()
                ? accountUpdateDto.username().get()
                : accountEntity.username(),
            accountUpdateDto.password().isPresent()
                ? BCrypt.hashpw(accountUpdateDto.password().get(), BCrypt.gensalt())
                : accountEntity.password(),
            accountUpdateDto.roleIds().isPresent()
                ? accountUpdateDto.roleIds().get()
                : accountEntity.roleIds(),
            accountUpdateDto.isVerified().isPresent()
                ? accountUpdateDto.isVerified().get()
                : accountEntity.isVerified(),
            accountUpdateDto.verifiedAt().isPresent()
                ? accountUpdateDto.verifiedAt().get()
                : accountEntity.verifiedAt(),
            accountEntity.createdAt(),
            Instant.now()));
  }
}
