package hr.algebra.java_web.controller;


import hr.algebra.java_web.model.*;
import hr.algebra.java_web.repository.*;
import hr.algebra.java_web.utility.Encryption;
import hr.algebra.java_web.utility.LoginUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/musicWebShop")
@AllArgsConstructor
@SessionAttributes({"username", "firstName", "lastName", "email"})
public class UserController {
    private final JWUserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final AlbumRepository albumRepository;
    private LoginHistoryRepository loginHistoryRepository;



    @GetMapping("/login.html")
    public String getLogin(Model model, HttpServletRequest request) {
    if (LoginUtility.isLogged(request)){
    return "Error";
    }else{
        model.addAttribute("loginUserRequest", new JWUser());
        return "Login";}
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("loginUserRequest") JWUser loginUserRequest, Model model, HttpServletRequest request) throws NoSuchAlgorithmException {

        String emailToFind = loginUserRequest.getEmail();
        JWUser user = userRepository.FindUserByEmail(emailToFind);

        if (user != null) {
            if (Encryption.validatePassword(loginUserRequest.getPasswordHash(), user.getPasswordSalt(), user.getPasswordHash())) {

                HttpSession session = request.getSession();
                session.setAttribute("userType", "JWUser");
                session.setAttribute("id", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("firstName", user.getFirstName());
                session.setAttribute("lastName", user.getLastName());
                session.setAttribute("email", user.getEmail());
                saveLoginHistory(user.getId(), user.getFirstName(),user.getLastName(), request);



                return "redirect:/musicWebShop/bestSellers.html";
            }

            return "redirect:/musicWebShop/login.html";
        } else {

            Admin admin = adminRepository.FindAdminByEmail(emailToFind);
            if (admin != null) {
                if (Encryption.validatePassword(loginUserRequest.getPasswordHash(), admin.getPasswordSalt(), admin.getPasswordHash())) {

                    HttpSession session = request.getSession();
                    session.setAttribute("userType", "Admin");
                    session.setAttribute("id", admin.getId());
                    session.setAttribute("username", admin.getUsername());
                    session.setAttribute("firstName", admin.getFirstName());
                    session.setAttribute("lastName", admin.getLastName());
                    session.setAttribute("email", admin.getEmail());



                    return "redirect:/musicWebShop/albumCRUD.html";
                }
            }


            return "Login";
        }
    }

    private void saveLoginHistory(Long userId,String firstName,String lastName, HttpServletRequest request) {
        try {
            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setUserId(userId);
            loginHistory.setFirstName(firstName);
            loginHistory.setLastName(lastName);
            loginHistory.setLoginTime(new Timestamp(System.currentTimeMillis()));

            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            loginHistory.setIpAddress(ipAddress);

            loginHistoryRepository.save(loginHistory);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @GetMapping("/register.html")
    public String getRegister(Model model ,HttpServletRequest request) {
        if (LoginUtility.isLogged(request)){return "Error";}else {
        JWUser addUserRequest = new JWUser();
        model.addAttribute("addUserRequest", addUserRequest);
        return "Register";
    }}

    @PostMapping("/register")
    public String register(@ModelAttribute("addUserRequest") JWUser addUserRequest, RedirectAttributes redirectAttributes, HttpServletRequest request) throws NoSuchAlgorithmException {


        String salt = Encryption.createSalt(8);
        String password = addUserRequest.getPasswordHash();
        JWUser user = new JWUser();
        user.setId(addUserRequest.getId());
        user.setUsername(addUserRequest.getUsername());
        user.setFirstName(addUserRequest.getFirstName());
        user.setLastName(addUserRequest.getLastName());
        user.setEmail(addUserRequest.getEmail());

        user.setPasswordSalt(salt);
        user.setPasswordHash(Encryption.generateHash(password, salt));


        userRepository.save(user);

        HttpSession session = request.getSession();

        session.setAttribute("userType", "JWUser");
        session.setAttribute("id", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());
        session.setAttribute("email", user.getEmail());
        saveLoginHistory(user.getId(),user.getFirstName(),user.getLastName(), request);


        return "redirect:/musicWebShop/bestSellers.html";
    }
    @GetMapping("/createAdmin.html")
    public String getCreateAdmin(Model model,HttpServletRequest request) {
        if (LoginUtility.isAdmin(request)){
        Admin addAdminRequest = new Admin();
        model.addAttribute("addAdminRequest", addAdminRequest);
        return "CreateAdmin";}else {return "Error";}
    }


    @PostMapping("/createAdmin")
    public String createAdmin(@ModelAttribute("addAdminRequest") Admin addAdminRequest, RedirectAttributes redirectAttributes, HttpServletRequest request) throws NoSuchAlgorithmException {


        String salt = Encryption.createSalt(8);
        String password = addAdminRequest.getPasswordHash();
        Admin user = new Admin();
        user.setId(addAdminRequest.getId());
        user.setUsername(addAdminRequest.getUsername());
        user.setFirstName(addAdminRequest.getFirstName());
        user.setLastName(addAdminRequest.getLastName());
        user.setEmail(addAdminRequest.getEmail());

        user.setPasswordSalt(salt);
        user.setPasswordHash(Encryption.generateHash(password, salt));


        adminRepository.save(user);

        HttpSession session = request.getSession();
        session.setAttribute("userType", "Admin");
        session.setAttribute("id", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());
        session.setAttribute("email", user.getEmail());



        return "redirect:/musicWebShop/albumCRUD.html";
    }



    @GetMapping("/aboutUs.html")
    public String getAboutUs(Model model) {

        return "AboutUs";
    }

    @GetMapping("/me.html")
    public String getMe(Model model, HttpServletRequest request) {
       if (LoginUtility.isLogged(request)){
        HttpSession session = request.getSession();
        model.addAttribute("userType", session.getAttribute("userType"));
        model.addAttribute("id", session.getAttribute("id"));
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("firstName", session.getAttribute("firstName"));
        model.addAttribute("lastName", session.getAttribute("lastName"));
        model.addAttribute("email", session.getAttribute("email"));
        return "Me";
    }
        return "Error";
    }


    @GetMapping("/contact.html")
    public String getContact(Model model){
        return "Contact";
    }
    @GetMapping("/shoppingCart.html")
    public String getShoppingCart(Model model, HttpServletRequest request) {

            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("id");

            List<CartItem> cartItems;

            if (userId == null) {
                cartItems = retrieveCartItemsFromSession(request);
            } else {
                List<ShoppingCart> shoppingCartItems = shoppingCartRepository.findByJWUserId(userId);
                cartItems = new ArrayList<>();

                for (ShoppingCart item : shoppingCartItems) {
                    albumRepository.findById(item.getAlbumId()).ifPresent(album -> cartItems.add(new CartItem(album, Math.toIntExact(item.getNumberOfOrders()))));
                }
            }

            model.addAttribute("cartItems", cartItems);

            return "ShoppingCart";
        }


    private List<CartItem> retrieveCartItemsFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        List<CartItem> cartItems = new ArrayList<>();

        if (cart != null && !cart.isEmpty()) {
            for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
                long albumId = entry.getKey();
                Integer quantity = entry.getValue();
                if (quantity != null) {
                    albumRepository.findById(albumId).ifPresent(album -> cartItems.add(new CartItem(album, quantity)));
                }
            }
        }

        return cartItems;
    }



    @ModelAttribute("httpServletRequest")
    public HttpServletRequest getCurrentRequest(HttpServletRequest request) {
        return request;
    }
}
