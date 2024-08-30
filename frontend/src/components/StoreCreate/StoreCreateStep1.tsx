import { useState, useEffect } from "react";
import { InputLabel } from "../common/InputLabel";
import { Input } from "../common/Input";
import { PhotoUploader } from "../common/PhotoUploader";
import { TextArea } from "../common/TextArea";
import { useBoundStore } from "../../store/store";

interface StoreCreateStep1Props {
  setIsFormComplete: (isComplete: boolean) => void;
}

const StoreCreateStep1: React.FC<StoreCreateStep1Props> = ({
  setIsFormComplete,
}) => {
  const {
    storeName,
    selectedPhotos,
    storeDescription,
    setStoreName,
    setSelectedPhotos,
    setStoreDescription,
  } = useBoundStore();

  const handlePhotosChange = (photos: File[]) => {
    setSelectedPhotos(photos);
  };

  useEffect(() => {
    const isComplete =
      storeName !== "" && storeDescription !== "" && selectedPhotos.length > 0;
    setIsFormComplete(isComplete);
  }, [storeName, selectedPhotos, storeDescription, setIsFormComplete]);

  return (
    <div>
      <div className="mb-4">
        <InputLabel name="스토어 이름" />
        <Input
          name="스토어 이름"
          value={storeName}
          maxLength={50}
          onChange={(e) => setStoreName(e.target.value)}
        />
      </div>
      <div className="mb-4">
        <InputLabel name="스토어 사진" />
        <div className="ml-4">
          <PhotoUploader
            maxPhotos={6}
            initialPhotos={selectedPhotos}
            onPhotosChange={handlePhotosChange}
          />
        </div>
      </div>
      <div className="mb-4">
        <InputLabel name="스토어 설명" />
        <TextArea
          name="스토어 설명"
          value={storeDescription}
          maxLength={1000}
          onChange={(e) => setStoreDescription(e.target.value)}
        />
      </div>
    </div>
  );
};

export default StoreCreateStep1;
