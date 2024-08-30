import { useState, useEffect } from "react";
import { Layout } from "../components/common/Layout";
import { ImageSlider } from "../components/common/ImageSlider";
import { IoLocationOutline, IoTimeOutline } from "react-icons/io5";
import { Tag } from "../components/common/Tag";
import { Card } from "../components/common/Card";
import { useParams } from "react-router";
import { ImageObject, Store } from "../types/store";
import { getStoreInfo } from "../utils/api/store";

const StoreDetail = () => {
  const { storeId } = useParams<{ storeId?: string }>();
  const [store, setStore] = useState<Store>();
  const [storeImgList, setStoreImgList] = useState<string[]>([]);

  const handleStoreInfo = async () => {
    try {
      const storeData = await getStoreInfo(storeId);
      const imgList = storeData?.data?.images.map(
        (img: ImageObject) => img.originImageUrl,
      );

      setStore(storeData?.data);
      setStoreImgList(imgList);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    handleStoreInfo();
  }, [storeId]);

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        <ImageSlider images={storeImgList} />
        <div className="px-2">
          <div className="flex items-center justify-between">
            <h1 className="mt-2 text-3xl font-bold">{store?.name}</h1>
          </div>
          <div className="my-3">
            {store?.tags.map((tag: string) => (
              <Tag key={tag} className="mb-1 mr-1" content={tag} />
            ))}
          </div>
          <div className="flex flex-row">
            <p className="text-sm text-gray-400">{store?.operateStartAt} -</p>
            <p className="text-sm text-gray-400">{store?.operateEndAt}</p>
          </div>
          <p className="mt-2 flex items-start text-sm">
            <IoLocationOutline className="mr-1 mt-1" />
            {store?.location}
          </p>
          <div className="mb-4 mt-4">
            <h2 className="mb-2 text-lg font-semibold">운영 시간</h2>
            <div className="space-y-1">
              <p className="flex items-center text-sm">
                <IoTimeOutline className="mr-1" />
                월-금: 11:00 - 22:00
              </p>
              <p className="flex items-center text-sm">
                <IoTimeOutline className="mr-1" />
                토-일: 10:30 - 22:00
              </p>
            </div>
          </div>
          <hr />
          <div className="mb-4 mt-4">
            <h2 className="mb-2 text-lg font-semibold">팝업스토어 소개</h2>
            <p
              className="text-sm"
              dangerouslySetInnerHTML={{ __html: store?.description || "" }}
            ></p>
          </div>
          {/* <hr />
          <div className="mt-4">
            <h2 className="mb-4 text-lg font-semibold">상품 소개</h2>
            <div className="flex justify-center">
              <div className="grid grid-cols-2 gap-8">
                {stores.map((store) => (
                  <div
                    key={store.id}
                    className="cursor-pointer transition-transform duration-300 hover:scale-105"
                    onClick={() => handleProductDetail(store.id)}
                  >
                    <Card
                      variant="big"
                      title={store.title}
                      imageSrc={store.imageSrc}
                    />
                    <p className="text-sm">￦30,000원</p>
                  </div>
                ))}
              </div>
            </div>
          </div> */}
        </div>
      </div>
    </Layout>
  );
};

export default StoreDetail;
