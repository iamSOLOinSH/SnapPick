import React, { useState, useRef, useEffect } from "react";
import { IoMdArrowBack, IoMdArrowForward } from "react-icons/io";

type ImageSliderProps = {
  images: string[];
};

export const ImageSlider = ({ images }: ImageSliderProps) => {
  const [currentIndex, setCurrentIndex] = useState(1);
  const [startX, setStartX] = useState(0);
  const [isDragging, setIsDragging] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);
  const transitionRef = useRef(true);

  const extendedImages = [images[images.length - 1], ...images, images[0]];

  const handleTouchStart = (e: React.TouchEvent) => {
    setStartX(e.touches[0].clientX);
    setIsDragging(true);
  };

  const handleMouseDown = (e: React.MouseEvent) => {
    setStartX(e.clientX);
    setIsDragging(true);
  };

  const handleTouchMove = (e: React.TouchEvent) => {
    if (!isDragging) return;
    const currentX = e.touches[0].clientX;
    handleDrag(currentX);
  };

  const handleMouseMove = (e: React.MouseEvent) => {
    if (!isDragging) return;
    const currentX = e.clientX;
    handleDrag(currentX);
  };

  const handleDragEnd = () => {
    setIsDragging(false);
  };

  const handleNextImage = () => {
    setCurrentIndex((prevIndex) => prevIndex + 1);
  };

  const handlePreviousImage = () => {
    setCurrentIndex((prevIndex) => prevIndex - 1);
  };

  const handleDrag = (currentX: number) => {
    const diff = startX - currentX;
    if (Math.abs(diff) > 50) {
      if (diff > 0) {
        handleNextImage();
      } else {
        handlePreviousImage();
      }
      setIsDragging(false);
    }
  };

  useEffect(() => {
    if (containerRef.current) {
      containerRef.current.style.transition = transitionRef.current
        ? "transform 300ms ease-out"
        : "";
      containerRef.current.style.transform = `translateX(-${currentIndex * 100}%)`;
    }
  }, [currentIndex]);

  useEffect(() => {
    if (currentIndex === 0) {
      transitionRef.current = false;
      setTimeout(() => {
        setCurrentIndex(extendedImages.length - 2);
      }, 300);
    } else if (currentIndex === extendedImages.length - 1) {
      transitionRef.current = false;
      setTimeout(() => {
        setCurrentIndex(1);
      }, 300);
    } else {
      transitionRef.current = true;
    }
  }, [currentIndex, extendedImages.length]);

  return (
    <div className="relative w-full overflow-hidden">
      <div
        ref={containerRef}
        className="flex cursor-pointer"
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleDragEnd}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleDragEnd}
        onMouseLeave={handleDragEnd}
      >
        {extendedImages.map((image, index) => (
          <div key={index} className="h-96 w-full flex-shrink-0 rounded-lg p-4">
            <img
              src={image}
              alt={`이미지 ${index}`}
              className="pointer-events-none h-full w-full select-none rounded-md object-cover"
            />
          </div>
        ))}
      </div>
      <div className="absolute bottom-7 left-1/2 flex -translate-x-1/2 space-x-2">
        {images.map((_, index) => (
          <button
            key={index}
            className={`h-2 w-2 rounded-full ${
              index === currentIndex - 1 ? "bg-primary" : "bg-darkBase"
            }`}
            onClick={() => setCurrentIndex(index + 1)}
          />
        ))}
      </div>
      <button
        className="absolute left-5 top-1/2 -translate-y-1/2 rounded-full bg-base p-2"
        onClick={handlePreviousImage}
      >
        <IoMdArrowBack />
      </button>
      <button
        className="absolute right-5 top-1/2 -translate-y-1/2 rounded-full bg-base p-2"
        onClick={handleNextImage}
      >
        <IoMdArrowForward />
      </button>
    </div>
  );
};
