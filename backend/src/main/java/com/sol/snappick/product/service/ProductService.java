package com.sol.snappick.product.service;

import com.sol.snappick.product.dto.ProductCreateReq;
import com.sol.snappick.product.dto.ProductDetailRes;
import com.sol.snappick.product.dto.ProductSimpleRes;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import com.sol.snappick.product.entity.ProductOption;
import com.sol.snappick.product.exception.ProductImageLimitExceedException;
import com.sol.snappick.product.exception.ProductNotFoundException;
import com.sol.snappick.store.exception.StoreNotFoundException ;
import com.sol.snappick.product.mapper.ProductImageMapper;
import com.sol.snappick.product.mapper.ProductMapper;
import com.sol.snappick.product.mapper.ProductOptionMapper;
import com.sol.snappick.product.repository.*;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.repository.StoreRepository;
import com.sol.snappick.util.ImageUploadRes;
import com.sol.snappick.util.MinioUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductImageRepository productImageRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductOptionMapper productOptionMapper;

    private final String BUCKET_NAME = "snappick-product";
    private final MinioUtil minioUtil;

    @Transactional
    public ProductDetailRes createProduct(
            Integer storeId,
            ProductCreateReq productCreateReq,
            MultipartFile[] images
    ) throws Exception{

        // 유효성 검증
        // 1) 팝업스토어
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        // 2) 이미지
        if (images!=null && images.length>10){
            throw new ProductImageLimitExceedException();
        }

        //Product 엔티티를 생성한다.
        Product productToCreate = productMapper.toEntity(productCreateReq);
        productToCreate = productRepository.save(productToCreate);

        //Store
        productToCreate.setStore(store);

        //이미지 처리 및 저장
        if (images!=null){
            List<ProductImage> productImages = uploadImagesToMinio(images, productToCreate);
            productImageRepository.saveAll(productImages);
            productToCreate.setImages(productImages);
        }

        //옵션 처리
        if (productCreateReq.getOptions()!=null){
            List<ProductOption> productOptions = productOptionMapper.toEntityList(productCreateReq.getOptions(), productToCreate);
            productOptionRepository.saveAll(productOptions);
            productToCreate.setOptions(productOptions);
        }

        //최종 DTO로 젼환하여 반환
        return productMapper.toDetailDto(productToCreate);
    }

    @Transactional
    public ProductDetailRes readProduct(
            Integer productId
    ) throws Exception{

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        return productMapper.toDetailDto(product);
    }

    @Transactional
    public List<ProductSimpleRes> readStoreProducts(Integer storeId)
            throws Exception{

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        return productRepository.findByStore(store).stream()
                .map(productMapper::toSimpleDto)
                .toList();
    }

    @Transactional
    public ProductDetailRes updateProduct(
            Integer productId,
            ProductCreateReq productCreateReq,
            MultipartFile[] images
    ) throws Exception{

        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        productToUpdate.updateDetails(
                productCreateReq.getName(),
                productCreateReq.getDescription(),
                productCreateReq.getPrice(),
                productCreateReq.getDailyLimit(),
                productCreateReq.getPersonalLimit()
        );

        //이미지 변경 내역이 있다면
        if (images!=null && images.length>0){

            //기존 이미지 삭제
            for (ProductImage image: productToUpdate.getImages()){
                minioUtil.deleteImage(image.getOriginImageUrl());
                minioUtil.deleteImage(image.getThumbnailImageUrl());
            }
            productImageRepository.deleteAll(productToUpdate.getImages());
            productToUpdate.getImages().clear();

            //이미지 저장
            List<ProductImage> productImages = uploadImagesToMinio(images, productToUpdate);
            productImageRepository.saveAll(productImages);
            productToUpdate.setImages(productImages);
        }

        //옵션 변경 내역이 있다면
        if (productCreateReq.getOptions()!=null) {

            //기존 옵션 삭제
            productOptionRepository.deleteAll(productToUpdate.getOptions());
            productToUpdate.getOptions().clear();

            //옵션 저장
            List<ProductOption> productOptions = productOptionMapper.toEntityList(
                    productCreateReq.getOptions(), productToUpdate
            );
            productOptionRepository.saveAll(productOptions);
            productToUpdate.setOptions(productOptions);

        }

        Product updatedProduct = productRepository.save(productToUpdate);
        return productMapper.toDetailDto(updatedProduct);
    }


    @Transactional
    public boolean deleteProduct(
            Integer productId
    ) throws Exception{

        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        //이미지 삭제
        for (ProductImage image: productToDelete.getImages()){
            minioUtil.deleteImage(image.getOriginImageUrl());
            minioUtil.deleteImage(image.getThumbnailImageUrl());
        }
        productImageRepository.deleteAll(productToDelete.getImages());

        //옵션 삭제
        productOptionRepository.deleteAll(productToDelete.getOptions());

        try {
            productRepository.delete(productToDelete);
            return true;
        } catch(Exception  e){
            return false;
        }

    }

    private List<ProductImage> uploadImagesToMinio(
            MultipartFile[] images,
            Product product
    ) throws Exception {
        List<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }

            ImageUploadRes imageDto = minioUtil.uploadImageWithThumbnail(
                    BUCKET_NAME, image); // 이미지 저장 메서드 필요
            if (imageDto == null) {
                continue;
            }
            ProductImage productImage = ProductImage.builder()
                    .originImageUrl(imageDto.getOriginImageUrl())
                    .thumbnailImageUrl(imageDto.getThumbnailImageUrl())
                    .product(product)
                    .build();
            productImages.add(productImage);
        }
        return productImages;
    }

}
