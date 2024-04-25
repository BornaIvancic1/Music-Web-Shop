package hr.algebra.java_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/musicWebShop")
@AllArgsConstructor
@SessionAttributes({"username", "firstName", "lastName", "email"})

public class LogoutController {
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }


        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");

        return "redirect:/musicWebShop/login.html";
    }
    @GetMapping("/error.html")
    public String getError(Model model) {

        return "Error";
    }

}
