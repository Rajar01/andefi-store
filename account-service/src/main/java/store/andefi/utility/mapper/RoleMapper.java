package store.andefi.utility.mapper;

import store.andefi.dto.RoleDto;
import store.andefi.entity.Role;

public final class RoleMapper {
  public static RoleDto toDto(Role role) {
    return new RoleDto(role.id().toString(), role.name());
  }
}
