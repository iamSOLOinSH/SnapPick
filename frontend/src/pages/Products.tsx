import { useState } from "react";
import { useNavigate } from "react-router";

import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { Blob_2 } from "../components/common/Background/Blob_2";

import { ProductDetail } from "../components/Product/ProductDetail";

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  thumbnailImageUrls: string[];
}

const initialProducts: Product[] = [
  {
    id: 0,
    name: "물품 1asdasdasdasdasdasds",
    price: 10000,
    stock: 20,
    thumbnailImageUrls: [
      "https://via.placeholder.com/150",
      "https://shopping-phinf.pstatic.net/main_8659887/86598875807.1.jpg?type=f300",
    ],
  },
  {
    id: 1,
    name: "물품 2",
    price: 20000,
    stock: 10,
    thumbnailImageUrls: ["https://via.placeholder.com/150"],
  },
  {
    id: 2,
    name: "물품 3",
    price: 20000,
    stock: 10,
    thumbnailImageUrls: ["https://via.placeholder.com/150"],
  },
  {
    id: 3,
    name: "물품 4",
    price: 20000,
    stock: 10,
    thumbnailImageUrls: ["https://via.placeholder.com/150"],
  },
  {
    id: 4,
    name: "물품 5",
    price: 20000,
    stock: 10,
    thumbnailImageUrls: ["https://via.placeholder.com/150"],
  },
];

const Products = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState<Product[]>(initialProducts);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);

  const handleProductClick = (product: Product) => {
    setSelectedProduct(product);
  };

  const closeModal = () => {
    setSelectedProduct(null);
  };

  return (
    <Layout className="bg-white px-0">
      <div className="relative">
        <div className="absolute right-[-105px] top-[-262px] z-0 rotate-45 animate-moveBlob1">
          <Blob_2 />
        </div>
      </div>
      <div className="mt-4 flex flex-col items-center">
        <h2 className="z-10 flex-grow text-center text-2xl font-bold">
          상품 정보
        </h2>
        <div className="my-8 grid h-[500px] grid-cols-2 gap-4 overflow-y-auto p-4 scrollbar-hide">
          {products.map((product: Product) => (
            <div
              key={product.id}
              onClick={() => handleProductClick(product)}
              className="flex w-full max-w-xs flex-col items-center rounded-md border bg-white p-4 shadow-md"
            >
              <img
                src={product.thumbnailImageUrls[0]}
                alt={product.name}
                className="mb-4 h-32 w-32 object-cover"
              />
              <h3 className="mb-2 w-full overflow-hidden text-ellipsis whitespace-nowrap text-center text-xl font-bold">
                {product.name}
              </h3>
              <p className="text-lg text-gray-700">
                {product.price.toLocaleString()}원
              </p>
            </div>
          ))}
        </div>
        <div className="mb-8 w-full px-4">
          <Button content="장바구니로 가기" onClick={() => navigate("/cart")} />
        </div>
      </div>
      {selectedProduct && (
        <ProductDetail product={selectedProduct} onClose={closeModal} />
      )}
    </Layout>
  );
};

export default Products;
