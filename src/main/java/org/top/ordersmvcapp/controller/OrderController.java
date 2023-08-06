package org.top.ordersmvcapp.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.top.ordersmvcapp.controller.html.HtmlKey;
import org.top.ordersmvcapp.controller.html.MessageStyle;
import org.top.ordersmvcapp.entity.Client;
import org.top.ordersmvcapp.entity.Order;
import org.top.ordersmvcapp.entity.OrderProduct;
import org.top.ordersmvcapp.entity.Product;
import org.top.ordersmvcapp.service.ClientService;
import org.top.ordersmvcapp.service.OrderProductService;
import org.top.ordersmvcapp.service.OrderService;
import org.top.ordersmvcapp.service.ProductService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("order")
@RequiredArgsConstructor
@SessionAttributes({OrderController.ATTRIBUTE_NAME})
@Transactional
public class OrderController {

    static final String ATTRIBUTE_NAME = "oldId";
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final ClientService clientService;
    private final ProductService productService;

    // получение списка всех заказов
    @GetMapping("")
    public String orderList(Model model) {
        Iterable<Order> orders = orderService.getAll();  // получить все заказы
        if (!orders.iterator().hasNext()) {
            model.addAttribute(HtmlKey.MESSAGE, "Список заказов пуст");
            model.addAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        model.addAttribute("orders", orders); // передаем коллекцию заказов на страницу
        return "order/order-list";
    }

    // обработчики добавления нового заказа
    @GetMapping("new")
    public String newOrder(Model model) {
        Order newOrder = new Order();
        model.addAttribute("newOrder", newOrder);
        Iterable<Client> clients = clientService.getAll();
        model.addAttribute("clients", clients);
        return "order/order-add-form";
    }

    @PostMapping("new")
    public String newOrder(Order newOrder, RedirectAttributes ra) {
        orderService.create(newOrder);
        ra.addFlashAttribute(HtmlKey.MESSAGE, "Заказ добавлен успешно");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        return "redirect:/order";
    }

    @GetMapping("new/{id}")
    public String newOrder(@PathVariable Integer id, Model model) {
        Order newOrder = new Order();
        Client newOrderClient = clientService.getById(id).get();
        newOrder.setClient(newOrderClient);
        model.addAttribute("newOrder", newOrder);
        Iterable<Client> clients = clientService.getAll();
        model.addAttribute("clients", clients);
        return "order/order-add-form";
    }
    // обработчик details (аналог получения по id)
    @GetMapping("details/{id}")
    public String detailsOrder(@PathVariable Integer id, Model model) {
        Optional<Order> order = orderService.getById(id);
        if (order.isPresent() && order.get().getClient()!=null) {
            Optional <Client> client=clientService.getById(order.get().getClient().getId());
            Iterable<OrderProduct> orderProducts = orderProductService.getAllByOrderId(order.get().getId());
            model.addAttribute("client", client.get());
            model.addAttribute("order", order.get());
            model.addAttribute("orderProducts", orderProducts);
            return "order/order-details";
        }
        return "redirect:/order";
    }

    // обработчик для удаления заказа: GET path variable
    @GetMapping("delete/{id}")
    public String deleteOrder(@PathVariable Integer id, RedirectAttributes ra) {
        if(!orderProductService.getAllByOrderId(id).isEmpty()) {
            orderProductService.deleteAllByOrderId(id);
        }
        orderService.deleteById(id);   // удалили
        ra.addFlashAttribute(HtmlKey.MESSAGE, "заказ удален БЕЗВОЗВРАТНО");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        return "redirect:/order";
    }
    // обработчики изменения заказа
    @GetMapping("update/{id}")
    public String updateOrder(@PathVariable Integer id, Model model) {
        // обновляемый объект для заполнения на форме
        Optional<Order> order = orderService.getById(id);
        if (order.isPresent() && order.get().getClient()!=null) {
            Iterable<Client> clients=clientService.getAll();
            List<OrderProduct> orderProducts = orderProductService.getAllByOrderId(order.get().getId());
            model.addAttribute("clients", clients);
            model.addAttribute("order", order.get());
            model.addAttribute("orderProducts", orderProducts);
            return "order/order-update-form";
        }
        return "redirect:/order";
    }

    @PostMapping("update")
    public String updateOrder(Order updatedOrder,
                              RedirectAttributes ra) {
        Order order = orderService.update(updatedOrder);
        if (order == null) {
            ra.addFlashAttribute(HtmlKey.MESSAGE, "заказ не обновлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE, "заказ " + order + " обновлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/order";
    }
    // обработчик добавления товара в заказ
    @GetMapping("new-product/{id}")
    public String newProduct(@PathVariable Integer id, Model model){
        Optional<Order> order = orderService.getById(id);
        if (order.isPresent() && order.get().getClient()!=null) {
            OrderProduct orderProduct = new OrderProduct();
            Iterable<Product> products = productService.getAll();
            Optional<Client> client = clientService.getById(order.get().getClient().getId());
            model.addAttribute("order", order.get());
            model.addAttribute("client", client.get());
            model.addAttribute("products", products);
            model.addAttribute("orderProduct", orderProduct);
            return "order/order-product-add";
        }
        return "redirect:/order";
    }
    @PostMapping("new-product")
    public String newProduct(OrderProduct newOrderProduct, RedirectAttributes ra) {
        orderProductService.create(newOrderProduct);
        Integer orderId = newOrderProduct.getOrder().getId();
        ra.addFlashAttribute(HtmlKey.MESSAGE, "Товар добавлен успешно");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        return "redirect:/order/new-product/"+orderId;
    }
    // обработчик удаления товара в заказе
    @GetMapping("delete-product/{id}")
    public String deletePtoduct(@PathVariable Integer id, RedirectAttributes ra) {
        Integer orderId = orderProductService.getById(id).get().getOrder().getId();
        orderProductService.deleteById(id);      // удалили товар
        ra.addFlashAttribute(HtmlKey.MESSAGE, "товар удален");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        return "redirect:/order/update/"+orderId;
    }
    // обработчик обновления товара в заказе
    @GetMapping("update-product/{id}")
    public String updateProduct(@PathVariable Integer id, Model model){
        OrderProduct orderProduct = orderProductService.getById(id).get();
        Integer orderId = orderProduct.getOrder().getId();
        Optional<Order> order = orderService.getById(orderId);
        if (order.isPresent() && order.get().getClient()!=null) {
            Iterable<Product> products = productService.getAll();
            Optional<Client> client = clientService.getById(order.get().getClient().getId());
            model.addAttribute("order", order.get());
            model.addAttribute("client", client.get());
            model.addAttribute("products", products);
            model.addAttribute("orderProduct", orderProduct);
            model.addAttribute(ATTRIBUTE_NAME,id);
            return "order/order-product-update";
        }
        return "redirect:/order";
    }
    @PostMapping("update-product")
    public String updateProduct(OrderProduct orderProduct,
                                @ModelAttribute(ATTRIBUTE_NAME) Integer id,
                                RedirectAttributes ra) {
        orderProduct.setId(id);
        Integer orderId=orderProduct.getOrder().getId();

        orderProductService.update(orderProduct);
        ra.addFlashAttribute(HtmlKey.MESSAGE, "Товар обновлен успешно");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        return "redirect:/order/update/"+orderId;
    }
}
