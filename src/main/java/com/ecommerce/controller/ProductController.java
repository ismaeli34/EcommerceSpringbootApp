package com.ecommerce.controller;

import com.ecommerce.entity.ImageModel;
import com.ecommerce.entity.Product;
import com.ecommerce.payloads.ApiResponse;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = {"/addNewProduct"},consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Product addNewProduct(@RequestPart("product") Product product,
                                 @RequestPart("imageFile")MultipartFile[] file){
        try{
            Set<ImageModel> images = uploadImage(file);
            product.setProductImages(images);
            Product product1 = productService.addNewProduct(product);
            return  product1;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

//        @PostMapping(value = {"/addNewProduct"})
//    public Product addNewProduct(@RequestBody Product product
//                                 ){
//            Product product1 = productService.addNewProduct(product);
//            return  product1;
//
//        }



    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for(MultipartFile file:multipartFiles){
            ImageModel imageModel = new ImageModel(file.getOriginalFilename(),
                    file.getContentType(), file.getBytes());
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    @GetMapping({"/getProductDetailsById/{productId}"})
    public  Product getProductDetailsById(@PathVariable("productId")  Integer productId){
        return productService.getProductDetailsById(productId);
    }

    @GetMapping({"/getAllProducts"})
    public List<Product> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam(defaultValue = "") String searchkey
                                        ){
        List<Product> results = productService.getAllProducts(pageNumber, searchkey);
        System.out.println("Result size is "+ results.size());
        return results;

    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping({"/deleteProductDetails/{productId}"})
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Integer productId){
         productService.deleteProduct(productId);
         return new ResponseEntity<ApiResponse>(new ApiResponse("product deleted successfully",true), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
    public List<Product> getProductDetails(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,
                                  @PathVariable(name = "productId")
                                  Integer productId){

      return   productService.getProductDetails(isSingleProductCheckout,productId);




    }
}
