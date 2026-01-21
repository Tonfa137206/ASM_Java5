package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.OrderDAO;
import com.poly.assigment_java5.dao.OrderDetailDAO;
import com.poly.assigment_java5.dao.CartItemDAO;
import com.poly.assigment_java5.dao.AccountDAO;
import com.poly.assigment_java5.entity.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private CartItemDAO cartItemDAO;

    @Autowired
    private AccountDAO accountDAO;

    // Danh sách đơn hàng
    @GetMapping
    public String listOrders(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        List<Order> orders;
        if (account.getRole()) { // Admin xem tất cả
            orders = orderDAO.findAll();
        } else { // User chỉ xem của mình
            orders = orderDAO.findByAccountUsernameOrderByCreateDateDesc(account.getUsername());
        }

        model.addAttribute("orders", orders);
        model.addAttribute("isAdmin", account.getRole());
        return "user/order/list";  // templates/user/order/list.html
    }

    // Trang checkout (tạo đơn hàng)
    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemDAO.findByAccount(account);
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("order", new Order());  // Để bind form
        return "user/order/checkout";  // templates/user/order/checkout.html
    }

    // Tạo đơn hàng mới
    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để đặt hàng!");
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemDAO.findByAccount(account);
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        // Tạo order
        order.setAccount(account);
        order.setCreateDate(new Date());
        order.setStatus(0);  // 0: Chờ xử lý
        order.setTotal(0.0);  // Tạm thời, sau tính từ details

        Order savedOrder = orderDAO.save(order);

        double total = 0.0;
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice());  // Lưu giá tại thời điểm đặt
            orderDetailDAO.save(detail);

            total += item.getProduct().getPrice() * item.getQuantity();

            // Giảm stock sản phẩm (nếu có)
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            // productDAO.save(product); // Uncomment nếu muốn giảm stock
        }

        savedOrder.setTotal(total);
        orderDAO.save(savedOrder);

        // Xóa giỏ hàng sau khi đặt
        cartItemDAO.deleteByAccount(account);

        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công! Đơn hàng của bạn đang chờ xử lý.");
        return "redirect:/orders";
    }

    // Chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        Order order = orderDAO.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/orders";
        }

        // Kiểm tra quyền xem
        if (!account.getRole() && !order.getAccount().getUsername().equals(account.getUsername())) {
            return "redirect:/orders";
        }

        List<OrderDetail> orderDetails = orderDetailDAO.findByOrder(order);
        double total = orderDetails.stream()
                .mapToDouble(OrderDetail::getTotal)
                .sum();

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("total", total);
        return "user/order/detail";  // templates/user/order/detail.html
    }

    // Cập nhật trạng thái đơn hàng (chỉ admin)
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam Integer status,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getRole()) {
            redirectAttributes.addFlashAttribute("error", "Chỉ admin mới có quyền cập nhật trạng thái!");
            return "redirect:/orders";
        }

        Order order = orderDAO.findById(orderId).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            return "redirect:/orders";
        }

        order.setStatus(status);
        orderDAO.save(order);

        redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        return "redirect:/orders";
    }

    // Hủy đơn hàng (user hủy nếu status=0, admin có thể hủy bất kỳ)
    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        Order order = orderDAO.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/orders";
        }

        // User chỉ hủy nếu status=0 (chờ xử lý), admin hủy bất kỳ
        if (order.getStatus() == 0 || account.getRole()) {
            order.setStatus(3); // 3: Đã hủy
            orderDAO.save(order);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng này!");
        }

        return "redirect:/orders";
    }
}