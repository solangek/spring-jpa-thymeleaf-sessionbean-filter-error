package main.controllers;

import javax.validation.Valid;
import main.repo.User;
import main.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class UserController {

    /* we inject a property from the application.properties  file */
    @Value( "${demo.coursename}" )
    private String someProperty;

    /* we need it so  inject the User repo bean */
    @Autowired
    private UserRepository repository;

    private UserRepository getRepo() {
        return repository;
    }

    /*
    a controller that acts as a middleware (all requests go thru it before reaching others
     */
//    @GetMapping ("/**")
//    public String main1(User user, Model model) {
//        //model.addAttribute("course", someProperty);
//        //model.addAttribute("users", getRepo().findAll());
//        System.out.println("in /**");
//        // you can return any view, it is not really happening since next controller does it
//        return "error";
//    }

    @GetMapping("/")
    public String main(User user, Model model) {
        model.addAttribute("course", someProperty);
        model.addAttribute("users", getRepo().findAll());
        System.out.println("in /");
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }
        getRepo().save(user);
        model.addAttribute("users", getRepo().findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {

        User user = getRepo().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        getRepo().save(user);
        model.addAttribute("users", getRepo().findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {

        User user = getRepo().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        getRepo().delete(user);
        model.addAttribute("users", getRepo().findAll());
        return "index";
    }

    @GetMapping(value="/json")
    public String json (Model model) {
        return "json";
    }
    /**
     * a sample controller return the content of the DB in JSON format
     * @param model
     * @return
     */
    @GetMapping(value="/getjson")
    public @ResponseBody List<User> getAll(Model model) {

        return getRepo().findAll();
    }
}

