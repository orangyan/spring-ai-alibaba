
package com.alibaba.cloud.ai.reader.mongodb.converter;

import com.alibaba.cloud.ai.reader.mongodb.MongodbResource;
import org.springframework.ai.document.Document;

/**
 * @author Yongtao Tan
 * @version 1.0.0
 */
public interface DocumentConverter {

	Document convert(org.bson.Document mongoDocument, String database, String collection, MongodbResource properties);

}
