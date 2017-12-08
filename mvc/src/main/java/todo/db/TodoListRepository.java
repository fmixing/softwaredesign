package todo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.model.TodoListModel;

public interface TodoListRepository extends JpaRepository<TodoListModel, Integer> {
}