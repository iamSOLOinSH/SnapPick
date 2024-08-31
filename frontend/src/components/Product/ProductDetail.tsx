import { useState } from "react";

import { addCartItem } from "../../utils/api/cart";

import { Button } from "../common/Button";
import { ImageSlider } from "../common/ImageSlider";
import { NumberSelector } from "../common/NumberSelector";

import { useSnackbar } from "notistack";

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  thumbnailImageUrls: string[];
}

interface ProductModalProps {
  product: Product;
  onClose: () => void;
}

export const ProductDetail: React.FC<ProductModalProps> = ({
  product,
  onClose,
}) => {
  const { enqueueSnackbar } = useSnackbar();
  const [quantity, setQuantity] = useState<number>(1);

  const cartId = localStorage.getItem("cartId");

  console.log(cartId);

  const handleIncrease = () => {
    setQuantity((prev) => prev + 1);
  };

  const handleDecrease = () => {
    setQuantity((prev) => (prev > 1 ? prev - 1 : prev));
  };

  const handleCart = () => {
    if (cartId) {
      addCartItem(+cartId, product.id, quantity)
        .then(() => {
          enqueueSnackbar("장바구니에 담았습니다.", {
            variant: "success",
          });
          onClose();
        })
        .catch(() => {
          enqueueSnackbar("장바구니에 담는 중 문제가 발생했습니다.", {
            variant: "error",
          });
        });
    } else {
      enqueueSnackbar(
        "카트 ID를 찾을 수 없습니다. 나중에 다시 시도해 주세요.",
        {
          variant: "error",
        },
      );
    }
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-gray-500 bg-opacity-50"
      onClick={onClose}
    >
      <div
        className="relative w-96 rounded-lg bg-white p-8 shadow-lg"
        onClick={(e) => e.stopPropagation()}
      >
        <button
          onClick={onClose}
          className="absolute right-4 top-4 text-xl font-bold"
        >
          &times;
        </button>
        <ImageSlider
          images={
            product.thumbnailImageUrls[0]
              ? product.thumbnailImageUrls
              : ["https://s3.youm.me/snappick-product/no_product.png"]
          }
        />
        <h3 className="mb-2 ml-4 text-2xl font-bold">{product.name}</h3>
        <p className="mb-2 ml-4 text-lg text-gray-700">
          가격: {product.price.toLocaleString()}원
        </p>
        <div className="flex justify-end">
          <NumberSelector
            quantity={quantity}
            onDecrease={handleDecrease}
            onIncrease={handleIncrease}
          />
        </div>
        <Button content="추가" className="mt-4" onClick={handleCart} />
      </div>
    </div>
  );
};
