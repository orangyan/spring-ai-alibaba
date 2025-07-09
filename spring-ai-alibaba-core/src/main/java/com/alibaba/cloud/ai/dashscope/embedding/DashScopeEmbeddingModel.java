
package com.alibaba.cloud.ai.dashscope.embedding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.metadata.EmptyUsage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.AbstractEmbeddingModel;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.ai.embedding.observation.DefaultEmbeddingModelObservationConvention;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationContext;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationConvention;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationDocumentation;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

/**
 * DashScopeEmbeddingModel类继承自AbstractEmbeddingModel，用于实现具体的嵌入模型功能。
 * 该类主要负责定义和管理DashScope嵌入模型的元数据，如模型的唯一标识符（UID）、
 * 模型的友好名称（Friendly name）、模型的描述、嵌入向量的维度、模型的版本信息等。
 * 此外，该类还提供了序列化和反序列化嵌入模型对象的功能，以便于模型对象的存储和传输。
 */
public class DashScopeEmbeddingModel extends AbstractEmbeddingModel {

	private static final Logger logger = LoggerFactory.getLogger(DashScopeEmbeddingModel.class);

	private static final EmbeddingModelObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultEmbeddingModelObservationConvention();

	private final DashScopeEmbeddingOptions defaultOptions;

	private final RetryTemplate retryTemplate;

	private final DashScopeApi dashScopeApi;

	private final MetadataMode metadataMode;

	/**
	 * Observation registry used for instrumentation.
	 */
	private final ObservationRegistry observationRegistry;

	/**
	 * Conventions to use for generating observations.
	 */
	private EmbeddingModelObservationConvention observationConvention = DEFAULT_OBSERVATION_CONVENTION;

	public DashScopeEmbeddingModel(DashScopeApi dashScopeApi) {
		this(dashScopeApi, MetadataMode.EMBED);
	}

	public DashScopeEmbeddingModel(DashScopeApi dashScopeApi, MetadataMode metadataMode) {
		this(dashScopeApi, metadataMode,
				DashScopeEmbeddingOptions.builder()
					.withModel(DashScopeApi.DEFAULT_EMBEDDING_MODEL)
					.withTextType(DashScopeApi.DEFAULT_EMBEDDING_TEXT_TYPE)
					.build());
	}

	public DashScopeEmbeddingModel(DashScopeApi dashScopeApi, MetadataMode metadataMode,
			DashScopeEmbeddingOptions dashScopeEmbeddingOptions) {
		this(dashScopeApi, metadataMode, dashScopeEmbeddingOptions, RetryUtils.DEFAULT_RETRY_TEMPLATE);
	}

	public DashScopeEmbeddingModel(DashScopeApi dashScopeApi, MetadataMode metadataMode,
			DashScopeEmbeddingOptions dashScopeEmbeddingOptions, RetryTemplate retryTemplate) {
		this(dashScopeApi, metadataMode, dashScopeEmbeddingOptions, retryTemplate, ObservationRegistry.NOOP);
	}

	public DashScopeEmbeddingModel(DashScopeApi dashScopeApi, MetadataMode metadataMode,
			DashScopeEmbeddingOptions options, RetryTemplate retryTemplate, ObservationRegistry observationRegistry) {
		Assert.notNull(dashScopeApi, "DashScopeApi must not be null");
		Assert.notNull(metadataMode, "metadataMode must not be null");
		Assert.notNull(options, "options must not be null");
		Assert.notNull(retryTemplate, "retryTemplate must not be null");
		Assert.notNull(observationRegistry, "observationRegistry must not be null");

		this.dashScopeApi = dashScopeApi;
		this.metadataMode = metadataMode;
		this.defaultOptions = options;
		this.retryTemplate = retryTemplate;
		this.observationRegistry = observationRegistry;
	}

	@Override
	public float[] embed(Document document) {
		Assert.notNull(document, "Document must not be null");
		return this.embed(document.getFormattedContent(this.metadataMode));
	}

	@Override
	public EmbeddingResponse call(EmbeddingRequest request) {
		// Before moving any further, build the final request EmbeddingRequest,
		// merging runtime and default options.
		EmbeddingRequest embeddingRequest = buildEmbeddingRequest(request);

		DashScopeApi.EmbeddingRequest apiRequest = createRequest(embeddingRequest);

		var observationContext = EmbeddingModelObservationContext.builder()
			.embeddingRequest(embeddingRequest)
			.provider(DashScopeApiConstants.PROVIDER_NAME)
			.build();

		return EmbeddingModelObservationDocumentation.EMBEDDING_MODEL_OPERATION
			.observation(this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
					this.observationRegistry)
			.observe(() -> {
				DashScopeApi.EmbeddingList apiEmbeddingResponse = this.retryTemplate.execute(ctx -> {
					try {
						return this.dashScopeApi.embeddings(apiRequest).getBody();
					}
					catch (Exception e) {
						logger.error("Error embedding request: {}", request.getInstructions(), e);
						throw e;
					}
				});

				if (apiEmbeddingResponse == null) {
					logger.warn("No embeddings returned for request: {}", request);
					return new EmbeddingResponse(List.of());
				}

				if (apiEmbeddingResponse.message() != null) {
					logger.error("Error message returned for request: {}", apiEmbeddingResponse.message());
					throw new RuntimeException("Embedding failed: error code:" + apiEmbeddingResponse.code()
							+ ", message:" + apiEmbeddingResponse.message());
				}

				DashScopeApi.EmbeddingUsage usage = apiEmbeddingResponse.usage();

				Usage embeddingUsage = usage != null ? this.getDefaultUsage(usage) : new EmptyUsage();

				var metadata = generateResponseMetadata(apiRequest.model(), embeddingUsage);
				List<Embedding> embeddings = apiEmbeddingResponse.output()
					.embeddings()
					.stream()
					.map(e -> new Embedding(e.embedding(), e.textIndex()))
					.toList();

				EmbeddingResponse embeddingResponse = new EmbeddingResponse(embeddings, metadata);

				observationContext.setResponse(embeddingResponse);

				return embeddingResponse;
			});
	}

	private DefaultUsage getDefaultUsage(DashScopeApi.EmbeddingUsage usage) {
		return new DefaultUsage(usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens(), usage);
	}

	private EmbeddingRequest buildEmbeddingRequest(EmbeddingRequest embeddingRequest) {
		// Process runtime options
		DashScopeEmbeddingOptions runtimeOptions = null;
		if (embeddingRequest.getOptions() != null) {
			runtimeOptions = ModelOptionsUtils.copyToTarget(embeddingRequest.getOptions(), EmbeddingOptions.class,
					DashScopeEmbeddingOptions.class);
		}

		DashScopeEmbeddingOptions requestOptions = runtimeOptions == null ? this.defaultOptions
				: DashScopeEmbeddingOptions.builder()
					// Handle portable embedding options
					.withModel(ModelOptionsUtils.mergeOption(runtimeOptions.getModel(), this.defaultOptions.getModel()))
					.withDimensions(ModelOptionsUtils.mergeOption(runtimeOptions.getDimensions(),
							defaultOptions.getDimensions()))

					// Handle dashscope specific embedding options
					.withTextType(
							ModelOptionsUtils.mergeOption(runtimeOptions.getTextType(), defaultOptions.getTextType()))
					.build();

		return new EmbeddingRequest(embeddingRequest.getInstructions(), requestOptions);
	}

	private DashScopeApi.EmbeddingRequest createRequest(EmbeddingRequest request) {
		DashScopeEmbeddingOptions requestOptions = (DashScopeEmbeddingOptions) request.getOptions();
		return DashScopeApi.EmbeddingRequest.builder()
			.model(requestOptions.getModel())
			.texts(request.getInstructions())
			.textType(requestOptions.getTextType())
			.dimension(requestOptions.getDimensions())
			.build();
	}

	private EmbeddingResponseMetadata generateResponseMetadata(String model, Usage usage) {
		Map<String, Object> map = new HashMap<>();
		map.put("model", model);
		map.put("total-tokens", usage.getTotalTokens());

		return new EmbeddingResponseMetadata(model, usage, map);
	}

	/**
	 * Use the provided convention for reporting observation data
	 * @param observationConvention The provided convention
	 */
	public void setObservationConvention(EmbeddingModelObservationConvention observationConvention) {
		Assert.notNull(observationConvention, "observationConvention cannot be null");
		this.observationConvention = observationConvention;
	}

}
