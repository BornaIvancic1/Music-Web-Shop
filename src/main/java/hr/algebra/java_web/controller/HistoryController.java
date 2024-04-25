package hr.algebra.java_web.controller;

import hr.algebra.java_web.model.LoginHistory;
import hr.algebra.java_web.repository.LoginHistoryRepository;
import hr.algebra.java_web.utility.LoginUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/musicWebShop")
@AllArgsConstructor
@SessionAttributes({"username", "firstName", "lastName", "email"})

public class HistoryController {

    private LoginHistoryRepository loginHistoryRepository;

    @GetMapping("/loginHistoryList.html")
    public String listLoginHistory(Model model, HttpServletRequest request) {
        if (LoginUtility.isAdmin(request)){
        List<LoginHistory> loginHistoryList = loginHistoryRepository.findAll();
        model.addAttribute("loginHistoryList", loginHistoryList);
        return "LoginHistoryList";
    }else {return "Error";}}


}
