package models

import Priority
import com.fasterxml.jackson.annotation.JsonProperty

data class Task(
    @JsonProperty(value = "id")
    var id: Int? = null,
    @JsonProperty(value = "name", required = true)
    val name: String,
    @JsonProperty(value = "priority", required = true)
    var priority: Priority,
    @JsonProperty(value = "completed")
    var completed: Boolean = false
)