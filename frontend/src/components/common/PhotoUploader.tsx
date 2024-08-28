import React, { useRef, useState, useEffect } from "react";
import { MdCameraAlt } from "react-icons/md";
import { IoClose } from "react-icons/io5";

interface Photo {
  file: File;
  preview: string;
}

interface PhotoUploaderProps {
  initialPhotos: File[];
  maxPhotos?: number;
  maxFileSize?: number;
  onPhotosChange: (photos: File[]) => void;
}

export const PhotoUploader: React.FC<PhotoUploaderProps> = ({
  initialPhotos,
  maxPhotos = 10,
  maxFileSize = 5, // 5MB
  onPhotosChange,
}) => {
  const [photos, setPhotos] = useState<Photo[]>([]);
  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const initialPhotoObjects = initialPhotos.map((file) => ({
      file,
      preview: URL.createObjectURL(file),
    }));
    setPhotos(initialPhotoObjects);

    return () => {
      initialPhotoObjects.forEach((photo) =>
        URL.revokeObjectURL(photo.preview),
      );
    };
  }, [initialPhotos]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const targetFiles = e.target.files as FileList;

    if (!targetFiles) return;

    const remainingSlots = maxPhotos - photos.length;
    const filesToProcess = Array.from(targetFiles).slice(0, remainingSlots);
    const oversizedFiles = filesToProcess.filter(
      (file) => file.size > maxFileSize * 1024 * 1024,
    );

    if (oversizedFiles.length > 0) {
      alert(`크기가 ${maxFileSize}MB를 초과할 수 없습니다!`);
    }

    if (targetFiles.length > remainingSlots) {
      alert(`최대 ${maxPhotos}개의 사진만 업로드할 수 있습니다.`);
    }

    const newPhotos = filesToProcess
      .filter((file) => file.size <= maxFileSize * 1024 * 1024)
      .map((file) => ({
        file,
        preview: URL.createObjectURL(file),
      }));

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

      <div className="w-full">
        <div className="flex space-x-2 overflow-x-auto py-2 scrollbar-hide">
          <button
            className="flex h-24 w-24 flex-shrink-0 flex-col items-center justify-center rounded-lg bg-base"
            onClick={handleClick}
          >
            <MdCameraAlt className="mb-2 text-2xl" />
            <span className="text-sm">
              {photos.length}/{maxPhotos}
            </span>
          </button>
          {photos.map((photo, i) => (
            <div key={photo.preview} className="relative mr-2 flex-shrink-0">
              <div className="relative h-24 w-24">
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
