package edu.whu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whu.demo.entity.Product;
import edu.whu.demo.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 使用MockMVC可以进行"单元"测试，其优势是：
 * （1）不用启动整个项目，可以部分加载需要测试的Bean进行测试
 * （2）可以使用MockBean来替代与外部的交互，从而测试可以不用启动外部依赖的环境
 */
@WebMvcTest({ProductController.class, ProductService.class})
public class ProductControllerMockMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductService productService;


    @AfterEach
    private void clean(){
        productService.deleteAll();
    }

    @BeforeEach
    private void initData(){
        Product product=new Product();
        product.setId(1);
        product.setName("IPhone 13");
        product.setPrice(8000);
        product.setProductType("SND-dff");
        product.setCategory("手机");
        product.setStockQuantity(20);
        productService.addProduct(product);
    }


    @Test
    public void testAddProduct() throws Exception {
        Product product=new Product();
        product.setId(2);
        product.setName("IPhone 18");
        product.setPrice(12000);
        product.setProductType("SND-dff");
        product.setCategory("手机");
        product.setStockQuantity(10);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/product")
                .content(new ObjectMapper().writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));

        //验证是否添加成功
        assertNotNull(productService.getProduct(2));

    }


    @Test
    public void testFindProduct() throws Exception {
        Product product=new Product();
        product.setId(2);
        product.setName("IPhone 18");
        product.setPrice(12000);
        product.setProductType("SND-dff");
        product.setCategory("手机");
        product.setStockQuantity(10);
        productService.addProduct(product);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get("/product/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(2));

        resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get("/product?name=IPhone&quantity=15")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(2));
    }


    @Test
    public void testUpdateProduct() throws Exception {
        Product product=  productService.getProduct(1);
        product.setStockQuantity(15);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .put("/product/1")
                .content(new ObjectMapper().writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //resultActions.andDo(print());
        resultActions.andExpect(status().isOk());

        //验证是否修改成功
        product=  productService.getProduct(1);
        assertEquals(15,product.getStockQuantity());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        assertNull(productService.getProduct(1));

    }


}
