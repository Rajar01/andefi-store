package store.andefi.core.embedding;

import store.andefi.core.exception.EmbeddingException;

public interface EmbeddingProvider {
    float[][] generateEmbeddingText(String text) throws EmbeddingException;
}
