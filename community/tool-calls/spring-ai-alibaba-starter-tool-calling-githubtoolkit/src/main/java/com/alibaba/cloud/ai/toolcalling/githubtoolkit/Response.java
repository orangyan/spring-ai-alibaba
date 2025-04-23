
package com.alibaba.cloud.ai.toolcalling.githubtoolkit;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription("GitHub API response")
public record Response<T>(T data) {
}
