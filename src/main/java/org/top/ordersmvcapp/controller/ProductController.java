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
import org.top.ordersmvcapp.entity.Product;
import org.top.ordersmvcapp.service.OrderProductService;
import org.top.ordersmvcapp.service.ProductService;

import java.util.Optional;

@Controller
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;  // сервис для работы с товарами
    private final OrderProductService orderProductService;

    // обработчик для получения списка товаров
    @GetMapping("")
    public String productList(Model model) {
        Iterable<Product> products = productService.getAll();  // получить все товары
        if (!products.iterator().hasNext()) {
            model.addAttribute(HtmlKey.MESSAGE, "Список товаров пуст");
            model.addAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        model.addAttribute("products", products); // передаем коллекцию товаров на страницу
        return "product/product-list";
    }
    // обработчик для добавления нового товара
    @GetMapping("new")
    public String newProduct(Model model){
        Product newProduct = new Product();
        model.addAttribute("newProduct",newProduct);
        return "product/product-add-form";
    }

    @PostMapping("new")
    public String newProduct(Product newProduct, RedirectAttributes ra){
        Product product = productService.register(newProduct);
        if (product == null) {
            ra.addFlashAttribute(HtmlKey.MESSAGE,"товар не добавлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE,"товар " + product + "добавлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/product";
    }
    // обработчик найти по id
    @GetMapping("details/{id}")
    public String detailsProduct(@PathVariable Integer id, Model model){
        Optional<Product> product = productService.getById(id);
        if(product.isPresent()){
            Iterable<OrderProduct> orderProducts = orderProductService.getAllByProductId(product.get().getId());
            model.addAttribute("orderProducts",orderProducts);
            return "product/product-details";
        }
        return "redirect:/product";
    }
    // обработчик для удаления товара: GET path variable
    @GetMapping("delete/{id}")
    public String deleteOrder(@PathVariable Integer id, RedirectAttributes ra) {
        productService.deleteById(id);   // удалили
        ra.addFlashAttribute(HtmlKey.MESSAGE, "товар удален БЕЗВОЗВРАТНО");
        ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.ERROR_STYLE);
        return "redirect:/product";
    }
    // обработчик для изменения товара
    @GetMapping("update/{id}")
    public String updateProduct(@PathVariable Integer id, Model model){
        Optional<Product> product = productService.getById(id);
        if(product.isPresent()) {
            model.addAttribute("product",product);
            return "product/product-update-form";
        }
        return "redirect:/product";
    }

    @PostMapping("update")
    public String updateProduct(Product updatedProduct, RedirectAttributes ra){
        Product product = productService.update(updatedProduct);
        if(product == null){
            ra.addFlashAttribute(HtmlKey.MESSAGE, "товар не обновлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE,MessageStyle.ERROR_STYLE);
        } else {
            ra.addFlashAttribute(HtmlKey.MESSAGE,"товар обновлен");
            ra.addFlashAttribute(HtmlKey.MESSAGE_STYLE, MessageStyle.SUCCESS_STYLE);
        }
        return "redirect:/product";
    }
}
