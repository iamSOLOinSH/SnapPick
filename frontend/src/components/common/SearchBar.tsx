import React, { useState, useRef, useEffect } from "react";
import { Input } from "./Input";
import { IoIosSearch } from "react-icons/io";

type SearchBarProps = {
  placeholder?: string;
  value: string;
  onChange: (value: string) => void;
  onSearch: () => void;
};

export const SearchBar: React.FC<SearchBarProps> = ({
  placeholder,
  value,
  onChange,
  onSearch,
}) => {
  const inputRef = useRef<HTMLInputElement>(null);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSearch();
    inputRef.current?.blur();
  };

  return (
    <form onSubmit={handleSubmit} className="relative w-full">
      <Input
        ref={inputRef}
        name="search"
        variant="full"
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="h-12 w-full rounded-full bg-gray-100 py-3 pl-6 pr-10 text-gray-700 shadow-md"
      />
      <button
        type="submit"
        className="absolute inset-y-0 right-4 flex cursor-pointer items-center bg-transparent"
      >
        <IoIosSearch className="text-xl text-gray-400" />
      </button>
    </form>
  );
};
