package com.sol.snappick.product.service;

import com.sol.snappick.member.exception.AccessDeniedException;
import com.sol.snappick.product.dto.ProductCreateReq;
import com.sol.snappick.product.dto.ProductDetailRes;
import com.sol.snappick.product.dto.ProductSimpleRes;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import com.sol.snappick.product.entity.ProductStatus;
import com.sol.snappick.product.exception.ProductImageLimitExceedException;
import com.sol.snappick.product.exception.ProductNotFoundException;
import com.sol.snappick.product.exception.QuantityException;
import com.sol.snappick.store.exception.StoreNotFoundException ;
import com.sol.snappick.product.mapper.ProductMapper;
import com.sol.snappick.product.repository.*;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.repository.StoreRepository;
import com.sol.snappick.util.minio.ImageUploadRes;
import com.sol.snappick.util.minio.MinioUtil;
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
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    private final ProductMapper productMapper;

    private final String BUCKET_NAME = "snappick-product";
    private final MinioUtil minioUtil;

    @Transactional
    public ProductDetailRes createProduct(
            Integer memberId,
            Integer storeId,
            ProductCreateReq productCreateReq,
            MultipartFile[] images
    ) throws Exception{

        // 유효성 검증
        // 1) 팝업스토어
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());
//        if (store.getMember().getId() != memberId)
//            throw new AccessDeniedException();

        // 2) 이미지
        if (images!=null && images.length>10){
            throw new ProductImageLimitExceedException();
        }

        // 3) 수량
        if (productCreateReq.getStock()<=0){
            throw new QuantityException("상품 등록 시 재고가 0일 수 없습니다.");
        }

        //Product 저장
        // name, description, price, stock, dailyLimit, personalLimit, images, store, status

        //name, description, price, stock, dailyLimit, personalLimit
        Product productToCreate = productMapper.toEntity(productCreateReq);
        //status
        productToCreate.setStatus(ProductStatus.판매가능);
        //store
        productToCreate.setStore(store);
        productToCreate = productRepository.save(productToCreate);

        //image
        if (images!=null){
            List<ProductImage> productImages = uploadImagesToMinio(images, productToCreate);
            productImageRepository.saveAll(productImages);
            productToCreate.setImages(productImages);
        }

        //최종 DTO로 젼환하여 반환
        return productMapper.toDetailDto(productToCreate);
    }

    @Transactional
    public ProductDetailRes readProduct(
            Integer productId
    ) throws Exception{
        //유효성 검증
        //1) product가 존재하는지 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        //Product
        //: name, description, price, stock, dailyLimit, personalLimit, images, store, status
        // >>
        //ProductDetailRes
        //: id, name, description, price, stock, dailyLimit, personalLimit, status, originImageUrls

        return productMapper.toDetailDto(product);
    }

    @Transactional
    public List<ProductSimpleRes> readStoreProducts(Integer storeId)
            throws Exception{
        //유효성 검증
        //1) store가 존재하는지 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        //Product
        //: name, description, price, stock, dailyLimit, personalLimit, images, store, status
        // >>
        //ProductSimpleRes
        //: id, name, price, stock, status, thumbnailImageUrls
        return productRepository.findByStore(store).stream()
                .map(productMapper::toSimpleDto)
                .toList();
    }

    @Transactional
    public ProductDetailRes updateProduct(Integer memberId, Integer productId, ProductCreateReq productCreateReq, MultipartFile[] images) throws Exception {

        //유효성 검증
        //1) 상품이 존재하는지 확인
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        //2) 수정 권한 확인
//        if (productToUpdate.getStore().getMember().getId() != memberId)
//            throw new AccessDeniedException();

        // 3) 이미지
        if (images!=null && images.length>10){
            throw new ProductImageLimitExceedException();
        }

        //Product 수정
        // name, description, price, stock, dailyLimit, personalLimit, images, store, status

        // name, description, price, stock, dailyLimit, personalLimit
        productToUpdate.updateDetails(
                productCreateReq.getName(),
                productCreateReq.getDescription(),
                productCreateReq.getPrice(),
                productCreateReq.getStock(),
                productCreateReq.getDailyLimit(),
                productCreateReq.getPersonalLimit()
        );

        //images
        if (images != null && images.length > 0) {
            //이전 데이터 삭제
            for (ProductImage image : new ArrayList<>(productToUpdate.getImages())) {
                minioUtil.deleteImage(image.getOriginImageUrl());
                minioUtil.deleteImage(image.getThumbnailImageUrl());
                productToUpdate.getImages().remove(image);
                productImageRepository.delete(image);
            }

            // 추가
            List<ProductImage> newImages = uploadImagesToMinio(images, productToUpdate);
            productToUpdate.getImages().addAll(newImages);
            productImageRepository.saveAll(newImages);
        }

        // store는 변경할 수 없다.

        //status
        if (productToUpdate.getStock()>0) productToUpdate.setStatus(ProductStatus.판매가능);
        else productToUpdate.setStatus(ProductStatus.품절);

        Product updatedProduct = productRepository.save(productToUpdate);
        return productMapper.toDetailDto(updatedProduct);
    }


    @Transactional
    public boolean deleteProduct(
            Integer memberId,
            Integer productId
    ) throws Exception{

        //유효성 검증
        //1) 상품이 존재하는지 확인
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        //2) 삭제 권한 확인
//        if (productToDelete.getStore().getMember().getId()!=memberId)
//            throw new AccessDeniedException();

        //Product 삭제
        // name, description, price, stock, dailyLimit, personalLimit, images, store, status

        //images
        for (ProductImage image: productToDelete.getImages()){
            minioUtil.deleteImage(image.getOriginImageUrl());
            minioUtil.deleteImage(image.getThumbnailImageUrl());
        }
        productImageRepository.deleteAll(productToDelete.getImages());

        try {
            //name, description, price, stock, dailyLimit, personalLimit, status
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
                    BUCKET_NAME, image);
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
