package todo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import todo.dao.TodosDao;
import todo.model.TodoModel;
import todo.model.CreateTodoListModel;
import todo.model.TodoListModel;
import todo.model.TodosModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TodoHandler {

    private final TodosDao todosDao;


    @Autowired
    public TodoHandler(TodosDao todosDao) {
        this.todosDao = todosDao;
    }


    @GetMapping("/todos")
    public String todos(Model model) {
        addToModel(model, new CreateTodoListModel());
        Map<String, TodoListModel> allTodoLists = todosDao.getAllTodoLists();

        List<TodoListModel> collect = new ArrayList<>(allTodoLists.values());

        addToModel(model, new TodosModel(collect));
        addToModel(model, new TodoModel());
        return "todos";
    }


    private void addToModel(Model model, CreateTodoListModel createTodoListModel) {
        model.addAttribute("createlist", createTodoListModel);
    }


    private void addToModel(Model model, TodosModel todosModel) {
        model.addAttribute("todolists", todosModel);
    }


    private void addToModel(Model model, TodoModel todoModel) {
        model.addAttribute("addtodo", todoModel);
    }


    @PostMapping("/todos")
    public String create(Model model, CreateTodoListModel createTodoListModel) {
        todosDao.addTodoList(createTodoListModel.getListName());
        return "redirect:/todos";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("name") String name) {
        todosDao.deleteTodoList(name);
        return "redirect:/todos";
    }


    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("addtodo") TodoModel todoModel, BindingResult bindingResult,
                      @RequestParam("name") String name) {
        if (!todoModel.getTodoname().equals("")) {
            todosDao.addTodo(name, todoModel.getTodoname());
        }
        return "redirect:/todos";
    }


    @PostMapping("/check")
    public String check(@RequestParam("name") String name, @RequestParam("todoname") String todoname) {
        todosDao.deleteTodo(name, todoname);
        return "redirect:/todos";
    }
}
