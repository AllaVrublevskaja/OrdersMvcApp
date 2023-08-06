package org.top.ordersmvcapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.top.ordersmvcapp.controller.html.HtmlKey;
import org.top.ordersmvcapp.controller.html.MessageStyle;
import org.top.ordersmvcapp.entity.OrderProduct;
import org.top.ordersmvcapp.service.OrderProductService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("orderProduct")
@RequiredArgsConstructor
public class OrderProductController {
    public final OrderProductService orderProductService;
    @GetMapping("")
    public String orderProductList(Model model) {
        List<OrderProduct> orderProducts = orderProductService.getAll();  // получить все
        if (orderProducts.isEmpty()) {
            model.addAttribute(HtmlKey.MESSAGE, "Список товаров пуст");
            model.addAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        model.addAttribute("orderProducts", orderProducts); // передаем коллекцию на страницу
        return "orderProduct/orderProduct-list";
    }
    // обработчик для добавления новой связи
    @GetMapping("new")
    public String newOrderProduct(Model model){
        OrderProduct newOrderProduct = new OrderProduct();
        model.addAttribute("newOrderProduct",newOrderProduct);
        return "orderProduct/orderProduct-add-form";
    }

    @PostMapping("new")
    public String newOrderProduct(OrderProduct newOrderProduct, RedirectAttributes ra){
        OrderProduct orderProduct = orderProductService.create(newOrderProduct);
        if (orderProduct == null) {
            ra.addFlashAttribute(HtmlKey.MESSAGE,"связь не добавлена");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE,"связь " + orderProduct + "добавлена");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/orderProduct";
    }
    // обработчик для удаления связи: GET path variable
    @GetMapping("delete/{id}")
    public String deleteOrderProduct(@PathVariable Integer id, RedirectAttributes ra) {
        orderProductService.deleteById(id);   // удалили
        ra.addFlashAttribute(HtmlKey.MESSAGE, "связь удалена БЕЗВОЗВРАТНО");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        return "redirect:/orderProduct";
    }
    // обработчик для изменения связи
    @GetMapping("update/{id}")
    public String updateOrderProduct(@PathVariable Integer id, Model model){
        Optional<OrderProduct> orderProduct = orderProductService.getById(id);
        if(orderProduct.isPresent()) {
            model.addAttribute("orderProduct",orderProduct);
            return "orderProduct/orderProduct-update-form";
        }
        return "redirect:/orderProduct";
    }

    @PostMapping("update")
    public String updateOrderProduct(OrderProduct updatedOrderProduct, RedirectAttributes ra){
        OrderProduct orderProduct = orderProductService.update(updatedOrderProduct);
        if(orderProduct == null){
            ra.addFlashAttribute(HtmlKey.MESSAGE, "связь не обновлена");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE,MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE,"связь обновлена");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/orderProduct";
    }
}
