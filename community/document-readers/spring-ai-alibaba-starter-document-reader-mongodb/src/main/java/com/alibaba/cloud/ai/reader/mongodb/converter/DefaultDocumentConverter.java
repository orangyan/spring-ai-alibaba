
package com.alibaba.cloud.ai.reader.mongodb.converter;

import com.alibaba.cloud.ai.reader.mongodb.MongodbResource;
import org.springframework.ai.document.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yongtao Tan
 * @version 1.0.0
 */
public class DefaultDocumentConverter implements DocumentConverter {

	@Override
	public Document convert(org.bson.Document mongoDocument, String database, String collection,
			MongodbResource properties) {
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("source", "mongodb");
		metadata.put("collection", collection);
		metadata.put("database", database);
		metadata.put("id", mongoDocument.getObjectId("_id").toString());

		// Add vectorization support
		if (properties.isEnableVectorization() && mongoDocument.containsKey(properties.getVectorField())) {
			metadata.put("vector", mongoDocument.get(properties.getVectorField()));
		}

		// Convert MongoDB document to string, excluding specific fields
		org.bson.Document contentDoc = new org.bson.Document(mongoDocument);
		contentDoc.remove("_id");
		contentDoc.remove(properties.getVectorField());
		String content = contentDoc.toJson();

		return new org.springframework.ai.document.Document(content, metadata);
	}

}
