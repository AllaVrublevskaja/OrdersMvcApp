package org.top.ordersmvcapp.controller;

import jakarta.transaction.Transactional;
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
import org.top.ordersmvcapp.entity.Client;
import org.top.ordersmvcapp.entity.Order;
import org.top.ordersmvcapp.entity.OrderProduct;
import org.top.ordersmvcapp.service.ClientService;
import org.top.ordersmvcapp.service.OrderProductService;
import org.top.ordersmvcapp.service.OrderService;

import java.util.*;

@Controller
@Transactional
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;  // сервис для работы с клиентами
    private final OrderService orderService;    //
    private final OrderProductService orderProductService;

    // обработчик для получения списка клиентов
    @GetMapping("")
    public String clientList(Model model) {
        Iterable<Client> clients = clientService.getAll();  // получить всех клиентов
        if (!clients.iterator().hasNext()) {
            model.addAttribute(HtmlKey.MESSAGE, "Список клиентов пуст");
            model.addAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        model.addAttribute("clients", clients); // передаем коллекцию клиентов на страницу
        return "client/client-list";
    }

    // обработчики добавления нового клиента
    @GetMapping("new")
    public String newClient(Model model) {
        // объект с пустыми полями для заполнения на форме
        Client newClient = new Client();
        model.addAttribute("newClient", newClient);
        return "client/client-add-form";
    }

    @PostMapping("new")
    public String newClient(Client newClient, RedirectAttributes ra) {
        Client client = clientService.register(newClient);
        if (client == null) {
            ra.addFlashAttribute(HtmlKey.MESSAGE, "клиент не добавлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE, "клиент " + client + " добавлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/client";
    }

    // обработчик для удаления клиента: GET path variable
    @GetMapping("delete/{id}")
    public String deleteClient(@PathVariable Integer id, RedirectAttributes ra) {
        Optional<Client> deleted = clientService.getById(id);
        if (deleted.isPresent()) {
            List<Order> deletedOrder = (List<Order>) orderService.getAllByClientId(id);
            if (!deletedOrder.isEmpty()) {
                for(Order d: deletedOrder) {
                    List<OrderProduct> deletedOp = orderProductService.getAllByOrderId(d.getId());
                    if (!deletedOp.isEmpty()) orderProductService.deleteAllByOrderId(d.getId());
                }
                orderService.deleteAllByClientId(id);
            }
            clientService.deleteById(id);
        }
        ra.addFlashAttribute(HtmlKey.MESSAGE, "клиент удален БЕЗВОЗВРАТНО");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        return "redirect:/client";
    }

    // обработчик details (аналог получения по id)
    @GetMapping("details/{id}")
    public String detailsClient(@PathVariable Integer id, Model model) {
        Optional<Client> client = clientService.getById(id);
        if (client.isPresent()) {
            Set<Order> order =client.get().getOrders();
            List<OrderProduct> orders=new ArrayList<>();
            for (Order o: order) {
                List<OrderProduct> newOP = (List<OrderProduct>) orderProductService.getAllByOrderId(o.getId());
                for(OrderProduct n: newOP)
                    orders.add(n);
            }
            model.addAttribute("client", client.get());
            model.addAttribute("orders", orders);
            return "client/client-details";
        }
        return "redirect:/client";
    }

    // обработчики изменения клиента
    @GetMapping("update/{id}")
    public String updateClient(@PathVariable Integer id, Model model) {
        // обновляемый объект для заполнения на форме
        Optional<Client> client = clientService.getById(id);
        if (client.isPresent()) {
            model.addAttribute("client", client);
            return "client/client-update-form";
        }
        return "redirect:/client";
    }

    @PostMapping("update")
    public String updateClient(Client updatedClient, RedirectAttributes ra) {
        Client client = clientService.update(updatedClient);
        if (client == null) {
            ra.addFlashAttribute(HtmlKey.MESSAGE, "клиент не обновлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE, "клиент " + client + " обновлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/client";
    }
}
