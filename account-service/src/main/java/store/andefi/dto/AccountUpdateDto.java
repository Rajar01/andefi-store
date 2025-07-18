package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;

public record AccountUpdateDto(
    ObjectId id,
    Optional<String> email,
    Optional<String> username,
    Optional<String> password,
    @JsonProperty("role_ids") Optional<List<String>> roleIds,
    @JsonProperty("is_verified") Optional<Boolean> isVerified,
    @JsonProperty("verified_at") Optional<Instant> verifiedAt) {}
