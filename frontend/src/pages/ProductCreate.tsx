import { useState } from "react";
import { Layout } from "../components/common/Layout";
import { InputLabel } from "../components/common/InputLabel";
import { Input } from "../components/common/Input";
import { PhotoUploader } from "../components/common/PhotoUploader";
import { Button } from "../components/common/Button";
import { useNavigate } from "react-router";
import { TextArea } from "../components/common/TextArea";
import { BackButton } from "../components/common/BackButton";
import { Blob_2 } from "../components/common/Background/Blob_2";

const ProductCreate = () => {
  const navigate = useNavigate();

  const [productName, setProductName] = useState("");
  const [productDescription, setProductDescription] = useState("");
  const [productPrice, setProductPrice] = useState("");
  const [productTotalStock, setProductTotalStock] = useState("");
  const [productDailyLimit, setProductDailyLimit] = useState("");
  const [productPersonalLimit, setProductPersonalLimit] = useState("");

  const [productPhotos, setProductPhotos] = useState<File[]>([]);
  const [isDailyLimit, setIsDailyLimit] = useState(true);
  const [isPersonalLimit, setIsPersonalLimit] = useState(true);

  const handlePhotosChange = (photos: File[]) => {
    setProductPhotos(photos);
  };

  const handleNumberInput = (
    e: React.ChangeEvent<HTMLInputElement>,
    setter: (value: string) => void,
  ) => {
    let value = e.target.value.replace(/\D/g, "");

    if (value.length > 9) {
      value = value.slice(0, 9);
    }

    if (parseInt(value) > 0 || value === "") {
      setter(value);
    }
  };

  const isFormComplete =
    productName !== "" &&
    productDescription !== "" &&
    productPhotos.length > 0 &&
    productPrice !== "" &&
    productTotalStock !== "" &&
    (isDailyLimit ? productDailyLimit !== "" : productDailyLimit === "") &&
    (isPersonalLimit
      ? productPersonalLimit !== ""
      : productPersonalLimit === "");

  const handleProductCreate = () => {
    navigate(`/product/create/success`);
  };

  return (
    <Layout>
      {/* 배경 박스들 */}
      <div className="relative">
        <div className="absolute right-[-160px] top-[-122px]">
          <Blob_2 />
        </div>
      </div>

      <div className="relative z-20 flex min-h-screen flex-col p-2">
        <div>
          <BackButton />
        </div>
        <h1 className="mb-4 mt-4 text-2xl font-bold">
          상품 정보를 입력해주세요.
        </h1>
        <div className="">
          <div className="mb-4">
            <InputLabel name="상품 이름" />
            <Input
              name="상품 이름"
              placeholder="상품 이름(최대 50자)"
              value={productName}
              onChange={(e) => setProductName(e.target.value)}
              maxLength={50}
            />
          </div>
          <div className="mb-4">
            <InputLabel name="상품 설명" />
            <TextArea
              name="상품 설명"
              placeholder="상품 설명(최대 255자)"
              value={productDescription}
              onChange={(e) => setProductDescription(e.target.value)}
              maxLength={255}
            />
          </div>
          <div className="mb-4 flex">
            <div className="w-full">
              <InputLabel name="가격" />
              <Input
                name="가격"
                className="w-3/4 placeholder:text-right"
                placeholder="원"
                variant="half"
                value={productPrice}
                onChange={(e) => handleNumberInput(e, setProductPrice)}
              />
            </div>
            <div className="w-full">
              <InputLabel name="수량" />
              <Input
                name="수량"
                className="w-3/4 placeholder:text-right"
                placeholder="개"
                variant="half"
                value={productTotalStock}
                onChange={(e) => setProductTotalStock(e.target.value)}
              />
            </div>
          </div>
          <div className="mb-4">
            <InputLabel name="상품 사진" />
            <div className="ml-4">
              <PhotoUploader onPhotosChange={handlePhotosChange} />
            </div>
          </div>
          <div className="mb-4 w-full">
            <InputLabel name="일일 판매 수량" />
            <div className="flex">
              <Input
                name="일일 판매 수량"
                className="w-1/2 placeholder:text-right"
                placeholder="개"
                variant="half"
                value={productDailyLimit}
                disabled={!isDailyLimit}
                onChange={(e) => handleNumberInput(e, setProductDailyLimit)}
              />
              <div className="flex items-center space-x-2 px-12 py-4">
                <Input
                  variant="check"
                  type="checkbox"
                  name="dailylimit-box"
                  checked={!isDailyLimit}
                  onChange={() => {
                    setIsDailyLimit((prev) => !prev);
                    setProductDailyLimit("");
                  }}
                />
                <span>해당없음</span>
              </div>
            </div>
          </div>
          <div className="mb-4 w-full">
            <InputLabel name="1인 구매 가능 개수" />
            <div className="flex">
              <Input
                name="1인 구매 가능 개수"
                className="w-1/2 placeholder:text-right"
                placeholder="개"
                variant="half"
                value={productPersonalLimit}
                disabled={!isPersonalLimit}
                onChange={(e) => handleNumberInput(e, setProductPersonalLimit)}
              />
              <div className="flex items-center space-x-2 px-12 py-4">
                <Input
                  variant="check"
                  type="checkbox"
                  name="personalLimit-box"
                  checked={!isPersonalLimit}
                  onChange={() => {
                    setIsPersonalLimit((prev) => !prev);
                    setProductPersonalLimit("");
                  }}
                />
                <span>해당없음</span>
              </div>
            </div>
          </div>
        </div>
        <div className="my-4">
          <Button
            content="완료"
            disabled={!isFormComplete}
            onClick={handleProductCreate}
          />
        </div>
      </div>
    </Layout>
  );
};

export default ProductCreate;
