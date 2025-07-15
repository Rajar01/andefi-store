package store.andefi.dto;

import java.util.Map;
import org.bson.types.ObjectId;

public record MediaDto(ObjectId id, Map<String, String> urls) {}
