package hr.algebra.java_web.config.paypal;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import hr.algebra.java_web.model.*;
import hr.algebra.java_web.repository.*;
import hr.algebra.java_web.utility.LoginUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PayPalController {

    private final PayPalService payPalService;
    private final  ShoppingCartRepository shoppingCartRepository;
    private final SoldShoppingCartRepository soldShoppingCartRepository;
    private final AlbumRepository albumRepository;
    private final JWUserRepository jWUserRepository;
    private final ShoppingCartDetailsRepository shoppingCartDetailsRepository;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");
        try {
            Double amount = shoppingCartRepository.amount(userId);
            String albumNames = shoppingCartRepository.albumNames(userId);

            model.addAttribute("amount", amount);
            model.addAttribute("albumNames", albumNames);
        } catch (Exception e) {
            log.error("Error occurred while fetching data for home page: ", e);
        }

        return "Home";
    }


    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("method") String method




            ,HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");
        try {
            if (method.equals("Cash")) {
                return new RedirectView("/locationConfirmation");
            } else {
        String cancelUrl="http://localhost:8080/payment/cancel";
        String successUrl="http://localhost:8080/payment/success";
        Double amount = shoppingCartRepository.amount(userId);
        String description = shoppingCartRepository.albumNames(userId);
            Payment payment=payPalService.createPayment(
                    amount,
                    "USD",
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl

            );
            for (Links links: payment.getLinks()){
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }


            }}

        }catch (PayPalRESTException e){
            log.error("Error occurred:: ",e);
        }
        return new RedirectView("/payment/error");

    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                shoppingCartRepository.transferToShoppingCartSold(userId);
                List<Long> soldShoppingCartIds = soldShoppingCartRepository.findIdsByJWUserId(userId);
                for (Long soldShoppingCartId : soldShoppingCartIds) {
                    shoppingCartDetailsRepository.insertDetails(soldShoppingCartId, LocalDateTime.now(), "PayPal");
                }
                return "PaymentSuccess";
            } else {
                return "PaymentError";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred during payment execution: ", e);
            return "PaymentError";
        }
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel(){
        return  "PaymentCancel";

    }

    @GetMapping("/locationConfirmation")
    public String paymentLocationConfirmation(){
        return  "PaymentLocationConfirmation";

    }
  @PostMapping("/locationConfirmation")
    public String paymentLocationConfirmation(
            @RequestParam("location") String location,
                                               HttpServletRequest request) {
      HttpSession session = request.getSession();
      Long userId = (Long) session.getAttribute("id");

      try {
          if (location != null) {


              shoppingCartRepository.transferToShoppingCartSold(userId);
              List<Long> soldShoppingCartIds = soldShoppingCartRepository.findIdsByJWUserId(userId);
              for (Long soldShoppingCartId : soldShoppingCartIds) {
                  shoppingCartDetailsRepository.insertDetails(soldShoppingCartId, LocalDateTime.now(), "Cash");
              }
              soldShoppingCartRepository.transferToLocation(
                      soldShoppingCartIds.stream().map(Object::toString).collect(Collectors.joining(", ")),
                      location);

              return "redirect:/payment/locationSuccess";
          } else {

              return "redirect:/payment/error";
              }

      } catch (Exception e) {
          log.error("Error occurred during payment execution: ", e);
          return "redirect:/payment/error";      }

  }

    @GetMapping("/payment/error")
    public String paymentError(){
        return  "PaymentError";

    }

    @GetMapping("/payment/locationSuccess")
    public String paymentLocationSuccess(){
        return  "PaymentLocationSuccess";

    }


    @GetMapping("/purchaseHistory")
    public String paymentPurchaseHistory(Model model, HttpServletRequest request) {
        if (LoginUtility.isUser(request)){
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");


            List<SoldShoppingCart> soldShoppingCartList = soldShoppingCartRepository.findByJWUserId(userId);
            List<SoldShoppingCartDTO> soldShoppingCartDTOList = new ArrayList<>();


        for (SoldShoppingCart soldShoppingCart : soldShoppingCartList) {
            Long albumId = soldShoppingCart.getAlbumId();
            Album album = albumRepository.findById(albumId).orElse(null);
            String albumName = album != null ? album.getTitle() : "Unknown Album";
            JWUser user=jWUserRepository.findById(userId).orElse(null);
            SoldShoppingCartDTO soldShoppingCartDTO = new SoldShoppingCartDTO();
            soldShoppingCartDTO.setSoldShoppingCart(soldShoppingCart);
            soldShoppingCartDTO.setAlbumName(albumName);
            soldShoppingCartDTO.setUser(user);

            List<ShoppingCartDetails> shoppingCartDetails = shoppingCartDetailsRepository.findBySoldShoppingCartId(soldShoppingCart.getId());
            soldShoppingCartDTO.setShoppingCartDetails(shoppingCartDetails);

            soldShoppingCartDTOList.add(soldShoppingCartDTO);
        }


            model.addAttribute("soldShoppingCartDTOList", soldShoppingCartDTOList);

        return "PurchaseHistory";
    }else  {
        return "Error";
    }}


    @GetMapping("/purchaseHistoryAll")
    public String paymentPurchaseHistoryAll(Model model,
                                            @RequestParam(required = false) Long customerId,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, HttpServletRequest request) {
        if (LoginUtility.isAdmin(request)){
        List<SoldShoppingCart> soldShoppingCartList;

        if (customerId != null && startDate != null && endDate != null) {
            soldShoppingCartList = soldShoppingCartRepository.findByCustomerIdAndPurchaseDateBetween(customerId, startDate, endDate);
        } else if (customerId != null) {
            soldShoppingCartList = soldShoppingCartRepository.findByJWUserId(customerId);
        } else if (startDate != null && endDate != null) {
            soldShoppingCartList = soldShoppingCartRepository.findByPurchaseDateBetween(startDate, endDate);
        } else {
            soldShoppingCartList = soldShoppingCartRepository.findAll();
        }

        List<SoldShoppingCartDTO> soldShoppingCartDTOList = new ArrayList<>();

        for (SoldShoppingCart soldShoppingCart : soldShoppingCartList) {
            Long albumId = soldShoppingCart.getAlbumId();
            Album album = albumRepository.findById(albumId).orElse(null);
            String albumName = album != null ? album.getTitle() : "Unknown Album";
            JWUser user=jWUserRepository.findById(soldShoppingCart.getjWUser_Id()).orElse(null);
            SoldShoppingCartDTO soldShoppingCartDTO = new SoldShoppingCartDTO();
            soldShoppingCartDTO.setSoldShoppingCart(soldShoppingCart);
            soldShoppingCartDTO.setAlbumName(albumName);
            soldShoppingCartDTO.setUser(user);

            List<ShoppingCartDetails> shoppingCartDetails = shoppingCartDetailsRepository.findBySoldShoppingCartId(soldShoppingCart.getId());
            soldShoppingCartDTO.setShoppingCartDetails(shoppingCartDetails);

            soldShoppingCartDTOList.add(soldShoppingCartDTO);
        }




        model.addAttribute("soldShoppingCartDTOList", soldShoppingCartDTOList);
        return "PurchaseHistoryAll";
    }else {
            return "Error";
        }}

}
