import Utils.Random
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import models.Task
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ToDoAppTest : Spek({
    Feature("Tasks") {

        Scenario("Create task and found task in tasks list") {
            val taskName = Random().getRandomString(8)
            val taskPriority = Priority.HIGH
            var taskList: Array<Task> = emptyArray()

            When("Create task") {
                Request.createTask(taskName, taskPriority)
            }
            Then("Get tasks list") {
                val response = Request.getTasks(false).body().string()
                taskList = jacksonObjectMapper().readValue(response)
            }
            And("Check if the task is in the list") {
                val actualTask = taskList.asList().stream().filter { x ->
                    x.name == taskName
                }.findFirst().get()

                assertTrue(actualTask.id != null)
                assertEquals(taskName, actualTask.name)
                assertEquals(taskPriority, actualTask.priority)
                assertEquals(false, actualTask.completed)
            }
        }

        Scenario("Completed tasks and check task in completed task list") {
            val taskName = Random().getRandomString(8)
            val taskPriority = Priority.HIGH
            val isCompleted = true
            val isNotCompleted = false
            var taskId: Int? = null
            var completedTaskList: Array<Task> = emptyArray()
            var taskList: Array<Task> = emptyArray()

            When("Create task") {
                Request.createTask(taskName, taskPriority)
            }
            And("Get data info") {
                val response = Request.getTasks(false).body().string()
                taskList = jacksonObjectMapper().readValue(response)
                val actualTask = taskList.asList().stream().filter { x ->
                    x.name == taskName
                }.findFirst().get()
                taskId = actualTask.id
            }
            And("Completed this task") {
                Request.completeTask(taskId, isCompleted)
            }
            And("Get completed tasks list") {
                val response = Request.getTasks(isCompleted).body().string()
                completedTaskList = jacksonObjectMapper().readValue(response)
            }
            And("Check if the task is in the list") {
                val actualTaskInCompletedTasksList = completedTaskList.asList().stream().filter { x ->
                    x.name == taskName
                }.findFirst().get()

                assertTrue(actualTaskInCompletedTasksList.id != null)
                assertEquals(taskName, actualTaskInCompletedTasksList.name)
                assertEquals(taskPriority, actualTaskInCompletedTasksList.priority)
                assertEquals(isCompleted, actualTaskInCompletedTasksList.completed)
            }
        }

        Scenario("To delete task") {
            val taskName = Random().getRandomString(8)
            val taskPriority = Priority.HIGH
            var taskList: Array<Task> = emptyArray()

            When("Create task") {
                Request.createTask(taskName, taskPriority)
            }
            Then("Get tasks list") {
                val response = Request.getTasks(false).body().string()
                taskList = jacksonObjectMapper().readValue(response)
            }

            lateinit var actualTaskBeforeDelete: Task

            And("Check if the task is in the list") {
                actualTaskBeforeDelete = taskList.asList().stream().filter { x ->
                    x.name == taskName
                }.findFirst().get()
            }

            When("To delete task") {
                actualTaskBeforeDelete.id?.let { Request.deleteTask(it) }
            }
            Then("Get tasks list") {
                val response = Request.getTasks(false).body().string()
                taskList = jacksonObjectMapper().readValue(response)
            }
            And("Check task in list") {
                val actualTask = taskList.asList().stream().filter { x ->
                    x.name == taskName
                }.findFirst().orElse(null)

                assertTrue(actualTaskBeforeDelete.id != null)
                assertEquals(taskName, actualTaskBeforeDelete.name)
                assertEquals(taskPriority, actualTaskBeforeDelete.priority)
                assertEquals(false, actualTaskBeforeDelete.completed)

                assertTrue(actualTask == null)
            }
        }

    }

})
