
package com.alibaba.cloud.ai.graph;

import java.util.function.BiFunction;

/**
 * Strategy interface for generating cache keys based on input parameters and the overall
 * state. This interface extends BiFunction, taking two generic input parameters to
 * produce a resulting key. The key generation logic can be affected by both input
 * parameters and the global state of the system, making it adaptable for various caching
 * scenarios within the project.
 *
 * @author disaster
 * @since 1.0.0.1
 */
public interface KeyStrategy extends BiFunction<Object, Object, Object> {

}
