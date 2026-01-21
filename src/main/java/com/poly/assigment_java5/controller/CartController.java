package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.CartItemDAO;
import com.poly.assigment_java5.dao.ProductDAO;
import com.poly.assigment_java5.dao.AccountDAO;
import com.poly.assigment_java5.entity.CartItem;
import com.poly.assigment_java5.entity.Account;
import com.poly.assigment_java5.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemDAO cartItemDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AccountDAO accountDAO;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemDAO.findByAccount(account);
        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "user/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm giỏ hàng!");
            return "redirect:/login";
        }

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemDAO.findByAccountAndProduct(account, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemDAO.save(item);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setAccount(account);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemDAO.save(cartItem);
        }

        redirectAttributes.addFlashAttribute("success", "Đã thêm vào giỏ hàng!");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Integer id,
                                 @RequestParam Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        CartItem cartItem = cartItemDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemDAO.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemDAO.save(cartItem);
        }

        redirectAttributes.addFlashAttribute("success", "Cập nhật giỏ hàng thành công!");
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        cartItemDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa khỏi giỏ hàng!");
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        cartItemDAO.deleteByAccount(account);
        redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng!");
        return "redirect:/cart";
    }

    @GetMapping("/count")
    @ResponseBody
    public String getCartItemCount(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "0";
        }

        int count = cartItemDAO.findByAccount(account).stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        return String.valueOf(count);
    }
}