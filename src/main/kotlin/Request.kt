import com.squareup.okhttp.*
import com.squareup.okhttp.Request

class Request {

    companion object {

        private val client = OkHttpClient()
        private val host = "127.0.0.1"
        private val port = 8080

        fun createTask(name: String, priority: Priority): Response {
            val url = HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("tasks")
                .build()

            val content = """
                {
                    "name": "$name",
                    "priority": "$priority"
                }
            """.trimIndent()

            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, content)
            val response = client.newCall(Request.Builder().url(url).post(body).build()).execute()
            assert(response.isSuccessful)

            return response
        }

        fun getTasks(completed: Boolean): Response {
            val url = HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("tasks")
                .addQueryParameter("done", completed.toString())
                .build()

            val response = client.newCall(Request.Builder().url(url).build()).execute()
            assert(response.isSuccessful)

            return response
        }

        fun completeTask(id: Int?, completed: Boolean): Response {
            if (id == null) {
                throw Exception("ID must not be null")
            }

            val content = ""
            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, content)

            val url = HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("tasks")
                .addPathSegment("status")
                .addPathSegment(id.toString())
                .addQueryParameter("done", completed.toString())
                .build()

            val response = client.newCall(Request.Builder().url(url).put(body).build()).execute()
            assert(response.isSuccessful)

            return response
        }

        fun deleteTask(id: Int): Response {
            val url = HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("tasks")
                .addPathSegment(id.toString())
                .build()

            val response = client.newCall(Request.Builder().url(url).delete().build()).execute()
            assert(response.isSuccessful)

            return response
        }

    }

}