package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TaskController에 존재하는 메소드를 Describe - Context - It 패턴으로
 * 각각의 메소드를 테스트 합니다.
 */
@DisplayName("TaskController 에서")
class TaskControllerTest {
    private static final String TASK_TITLE = "TaskTitle";
    private static final String UPDATE_TASK_TITLE = "UpdateTaskTitle";

    private TaskController controller;

    @BeforeEach
    void setUp() {
        this.controller = new TaskController(new TaskService());
    }

    @Nested
    @DisplayName("모든 Task 객체 조회 메소드는")
    class Describe_list_of_task {

        @Nested
        @DisplayName("Task 객체가 존재하지 않을 경우")
        class Context_with_empty_tasks {
            final long createTaskSize = 0L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("빈 배열을 리턴 한다")
            void it_return_empty_list() {
                List<Task> tasks = controller.list();
                assertThat(tasks).isEmpty();
                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("Task 객체가 존재할 경우")
        class Context_with_tasks {
            final long createTaskSize = 4L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("Task 객체가 존재하는 배열을 리턴 한다")
            void it_returns_task_list() {
                List<Task> tasks = controller.list();
                assertThat(tasks).isNotEmpty();
                assertThat(tasks).hasSize((int) createTaskSize);
            }
        }
    }

    @Nested
    @DisplayName("Task 객체를 ID 로 조회하는 API는")
    class Describe_detail_of_task {

        @Nested
        @DisplayName("ID 에 해당하는 Task가 존재하지 않을 경우")
        class Context_with_invalid_id {
            final long createTaskSize = 3L;
            final long requestTaskId = 4L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.detail(requestTaskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("ID 에 해당하는 Task가 존재할 경우")
        class Context_with_valid_id {
            final long createTaskSize = 7L;
            final long requestTaskId = 3L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }
            
            @Test
            @DisplayName("Task 객체를 리턴한다")
            void it_return_task() {
                Task foundTask = controller.detail(requestTaskId);

                assertThat(foundTask).isNotNull();
                assertThat(foundTask.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("Task 객체를 ID 로 업데이트 하는 메소드는")
    class Describe_update_of_task {

        @Nested
        @DisplayName("HttpMethod 가 PUT 일때")
        class Context_with_http_method_put {

            @Nested
            @DisplayName("ID 에 해당하는 Task 객체가 없을 경우")
            class Context_with_invalid_id {
                final long createTaskSize = 8L;
                final long requestTaskId = 10L;

                @BeforeEach
                void setUp() {
                    for (int i = 0; i < createTaskSize; i++) {
                        Task task = new Task();
                        task.setTitle(TASK_TITLE);
                        controller.create(task);
                    }
                }

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다")
                void it_throw_TaskNotFoundException() {
                    Task task = new Task();
                    task.setTitle(UPDATE_TASK_TITLE);

                    assertThatThrownBy(() -> controller.update(requestTaskId, task))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }

            @Nested
            @DisplayName("ID 에 해당하는 Task 객체가 있을 경우")
            class Context_with_valid_id {
                final long createTaskSize = 8L;
                final long requestTaskId = 5L;

                @BeforeEach
                void setUp() {
                    for (int i = 0; i < createTaskSize; i++) {
                        Task task = new Task();
                        task.setTitle(TASK_TITLE);
                        controller.create(task);
                    }
                }

                @Test
                @DisplayName("ID에 해당하는 Task 객체를 업데이트 후 반환한다")
                void it_return_updated_task() {
                    Task foundTask = controller.detail(requestTaskId);
                    foundTask.setTitle(UPDATE_TASK_TITLE);
                    Task updatedTask = controller.update(requestTaskId, foundTask);

                    assertThat(updatedTask).isNotNull();
                    assertThat(updatedTask).isEqualTo(foundTask);
                }
            }
        }

        @Nested
        @DisplayName("HttpMethod 가 PATCH 일때")
        class Context_with_http_method_patch {

            @Nested
            @DisplayName("ID 에 해당하는 Task 객체가 없을 경우")
            class Context_with_invalid_id {
                final long createTaskSize = 6L;
                final long requestTaskId = 14L;

                @BeforeEach
                void setUp() {
                    for (int i = 0; i < createTaskSize; i++) {
                        Task task = new Task();
                        task.setTitle(TASK_TITLE);
                        controller.create(task);
                    }
                }

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다")
                void it_throw_TaskNotFoundException() {
                    Task task = new Task();
                    task.setTitle(UPDATE_TASK_TITLE);
                    assertThatThrownBy(() -> controller.patch(requestTaskId, task))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }

            @Nested
            @DisplayName("ID 에 해당하는 Task 객체가 있을 경우")
            class Context_with_valid_id {
                final long createTaskSize = 6L;
                final long requestTaskId = 4L;

                @BeforeEach
                void setUp() {
                    for (int i = 0; i < createTaskSize; i++) {
                        Task task = new Task();
                        task.setTitle(TASK_TITLE);
                        controller.create(task);
                    }
                }

                @Test
                @DisplayName("ID에 해당하는 Task 객체를 업데이트 후 반환한다")
                void it_return_updated_task() {
                    Task foundTask = controller.detail(requestTaskId);
                    foundTask.setTitle(UPDATE_TASK_TITLE);
                    Task updatedTask = controller.patch(requestTaskId, foundTask);

                    assertThat(updatedTask).isNotNull();
                    assertThat(updatedTask).isEqualTo(foundTask);
                }
            }
        }
    }

    @Nested
    @DisplayName("Task 객체를 ID 로 삭제 하는 메소드는")
    class Describe_delete_of_task {

        @Nested
        @DisplayName("해당 ID의 Task가 존재하지 않을 경우")
        class Context_with_invalid_id {
            final long createTaskSize = 7L;
            final long requestTaskId = 10L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.delete(requestTaskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("ID 에 존재하는 Task 가 존재할 경우")
        class Context_with_valid_id {
            final long createTaskSize = 8L;
            final long requestTaskId = 7L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("ID 에 해당하는 테스크를 삭제한다")
            void it_response_no_content() {
                int oldSize = controller.list().size();

                controller.delete(requestTaskId);
                List<Task> tasks = controller.list();

                assertThat(tasks).isNotEmpty();
                assertThat(tasks.size()).isNotEqualTo(oldSize);
                assertThat(tasks).hasSize(oldSize - 1);
            }
        }
    }

}