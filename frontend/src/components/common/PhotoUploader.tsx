import React, { useRef, useState, useEffect } from "react";
import { MdCameraAlt } from "react-icons/md";
import { IoClose } from "react-icons/io5";

interface Photo {
  file: File;
  preview: string;
}

interface PhotoUploaderProps {
  maxPhotos?: number;
  maxFileSize?: number;
  onPhotosChange: (photos: File[]) => void;
}

export const PhotoUploader: React.FC<PhotoUploaderProps> = ({
  maxPhotos = 10,
  maxFileSize = 5 * 1024 * 1024, // 5MB
  onPhotosChange,
}) => {
  const [photos, setPhotos] = useState<Photo[]>([]);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const targetFiles = e.target.files as FileList;

    if (!targetFiles) return;

    const newPhotos = Array.from(targetFiles).reduce((item: Photo[], file) => {
      if (file.size > maxFileSize) {
        alert(`크기가 ${maxFileSize}MB를 초과할 수 없습니다!`);
        return item;
      }
      if (item.length + photos.length >= maxPhotos) {
        alert(`최대 ${maxPhotos}개의 사진만 업로드할 수 있습니다!`);
        return item;
      }
      item.push({
        file,
        preview: URL.createObjectURL(file),
      });
      return item;
    }, []);

    setPhotos((prev) => [...prev, ...newPhotos]);
    onPhotosChange([...photos, ...newPhotos].map((photo) => photo.file));
  };

  const handleDeletePhoto = (index: number) => {
    setPhotos((prev) => {
      const newPhotos = [...prev];
      URL.revokeObjectURL(newPhotos[index].preview);
      newPhotos.splice(index, 1);
      onPhotosChange(newPhotos.map((photo) => photo.file));
      return newPhotos;
    });
  };

  const handleClick = () => {
    fileInputRef?.current?.click();
  };

  return (
    <div className="flex flex-col items-start">
      <input
        type="file"
        accept="image/*"
        ref={fileInputRef}
        multiple
        className="hidden"
        onChange={handleFileChange}
      />

      <div className="mb-4 w-full">
        <div className="flex space-x-2 overflow-x-auto py-2">
          <button
            className="mb-4 flex h-20 w-20 flex-shrink-0 flex-col items-center justify-center rounded-lg bg-base"
            onClick={handleClick}
          >
            <MdCameraAlt className="mb-2 text-2xl" />
            <span className="text-sm">
              {photos.length}/{maxPhotos}
            </span>
          </button>
          {photos.map((photo, i) => (
            <div
              key={photo.preview}
              className="relative mr-2 flex-shrink-0 scrollbar-hide"
            >
              <div className="relative h-20 w-20">
                <img
                  src={photo.preview}
                  alt={`이미지 ${i + 1}`}
                  className="absolute inset-0 h-full w-full rounded-lg object-cover"
                />
                <button
                  className="absolute -right-2 -top-2 rounded-full border border-darkBase bg-white p-1 text-darkBase"
                  onClick={() => handleDeletePhoto(i)}
                >
                  <IoClose className="text-darkBase" />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
