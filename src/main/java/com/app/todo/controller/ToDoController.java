package com.app.todo.controller;


import com.app.todo.enums.Role;
import com.app.todo.model.ToDo;
import com.app.todo.model.User;
import com.app.todo.service.ToDoService;
import com.app.todo.service.UserService;
import com.app.todo.util.Constants;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ToDoController {

    private final ToDoService service;

    private final UserService userService;


    public ToDoController(ToDoService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping({"/", "/viewToDoList"})
    public String viewAllToDoItems(Model model, @ModelAttribute(Constants.MESSAGE_ATTRIBUTE) String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isEmpty()) {
            return "Home";
        }

        if (user.get().getRole().equals(Role.ADMIN)) {
            model.addAttribute("list", service.getAllToDoItems());
        } else {
            if (user.get().getTodos() == null || user.get().getTodos().isEmpty()) {
                user.get().setTodos(new ArrayList<>());
            }
            model.addAttribute("list", user.get().getTodos());
        }
        model.addAttribute(Constants.MESSAGE_ATTRIBUTE, Objects.requireNonNullElse(message, ""));
        return "ViewToDoList";
    }


    @GetMapping("/updateToDoStatus/{id}")
    public String updateToDoStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (service.updateStatus(id)) {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Updated Successfully");
            return "redirect:/viewToDoList";
        }
        redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Update Failed");
        return "redirect:/viewToDoList";
    }

    @GetMapping("/addToDoItem")
    public String addToDoItem(Model model) {
        model.addAttribute("todo", new ToDo());
        return "AddToDoItem";
    }

    @PostMapping("/saveToDoItem")
    public String saveToDoItem(@Valid @ModelAttribute(name = "todo") ToDo todo, BindingResult result, RedirectAttributes redirectAttributes, Model model) throws Exception {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date todayDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Save Failed");
            return Constants.REDIRECT_ADD_TO_DO_ITEM;
        } else if (todo.getDate().before(todayDate)) {
            model.addAttribute("todo", todo);
            result.rejectValue("date", "error.date", "Date must be in the future");
            return "AddToDoItem";
        }
        todo.setStatus("Incomplete");
        if (service.saveOrUpdateToDoItem(todo)) {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Save Successful");
            return Constants.REDIRECT_VIEW_TO_DO_LIST;
        }
        redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Save Failed");
        return Constants.REDIRECT_ADD_TO_DO_ITEM;
    }

    @GetMapping("/editToDoItem/{id}")
    public String editToDoItem(@PathVariable Long id, Model model) {
        model.addAttribute("todo", service.getToDoItemById(id));
        return "EditToDoItem";
    }

    @PostMapping("/editSaveToDoItem")
    public String editSaveToDoItem(@Valid @ModelAttribute(name = "todo") ToDo todo, BindingResult result, RedirectAttributes redirectAttributes, Model model) throws Exception {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date todayDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Edit Failed");
            return "redirect:/editToDoItem/" + todo.getId();
        } else if (todo.getDate().before(todayDate)) {
            model.addAttribute("todo", todo);
            result.rejectValue("date", "error.date", "Date must be in the future");
            return "EditToDoItem";
        }
        ToDo existingTodo = service.getToDoItemById(todo.getId());
        if (existingTodo != null) {
            todo.setStatus(existingTodo.getStatus());
        } else {
            todo.setStatus("Incomplete");
        }
        if (service.saveOrUpdateToDoItem(todo)) {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Edit Successful");
            return Constants.REDIRECT_VIEW_TO_DO_LIST;
        }

        redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Edit Failed");
        return "redirect:/editToDoItem/" + todo.getId();
    }

    @GetMapping("/deleteToDoItem/{id}")
    public String deleteToDoItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (service.deleteToDoItem(id)) {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Delete Successful");
        } else {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE_ATTRIBUTE, "Delete Failed");
        }
        return Constants.REDIRECT_VIEW_TO_DO_LIST;
    }


    @GetMapping("/searchToDo")
    public String searchToDos(@RequestParam String title, Model model) {
        model.addAttribute("list", service.findToDosByTitleContaining(title));
        model.addAttribute(Constants.MESSAGE_ATTRIBUTE, "");
        return "ViewToDoList";
    }

}

