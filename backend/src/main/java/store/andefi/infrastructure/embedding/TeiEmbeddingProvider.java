package store.andefi.infrastructure.embedding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import store.andefi.core.embedding.EmbeddingProvider;
import store.andefi.core.exception.EmbeddingException;

@ApplicationScoped
public class TeiEmbeddingProvider implements EmbeddingProvider {
    @Inject
    @RestClient
    TeiEmbeddingClient teiEmbeddingClient;

    @Override
    public float[][] generateEmbeddingText(String text) throws EmbeddingException {
        try {
            return teiEmbeddingClient.embed(new TeiEmbeddingRequest(text));
        } catch (Exception e) {
            throw new EmbeddingException("Failed to generate text embedding");
        }
    }
}


