import React, { useState, useEffect } from "react";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { SearchBar } from "../components/common/SearchBar";
import { Tag } from "../components/common/Tag";
import { useNavigate, useParams } from "react-router";
import { GoSortAsc, GoSortDesc } from "react-icons/go";
import { HiPlus } from "react-icons/hi";
import { getProducts } from "../utils/api/product";

interface Product {
  id: string;
  name: string;
  stock: number;
  status: "판매가능" | "품절";
}

const StockControl = () => {
  const navigate = useNavigate();

  const { storeId } = useParams<{ storeId?: string }>();
  const [sorting, setSorting] = useState<SortingState>([]);
  const [data, setData] = useState<Product[]>([]);
  const [filteredData, setFilteredData] = useState<Product[]>([]);
  const [searchTerm, setSearchTerm] = useState("");

  const columns = React.useMemo<ColumnDef<Product>[]>(
    () => [
      {
        accessorKey: "status",
        header: "상태",
        cell: (info) => {
          const status = info.getValue() as string;

          switch (status) {
            case "판매가능":
              return (
                <div className="flex flex-shrink-0">
                  <Tag content="판매중" variant="primary" />
                </div>
              );

            case "품절":
              return <Tag content="품절" variant="red" />;
          }
        },
      },
      {
        accessorFn: (row) => ({ name: row.name }),
        id: "productInfo",
        header: "상품명",
        cell: (info) => {
          const { name } = info.getValue() as {
            name: string;
          };
          return (
            <div className="flex items-center text-left">
              <span>{name}</span>
            </div>
          );
        },
      },

      {
        accessorKey: "stock",
        header: "수량",
        cell: (info) => {
          const value = info.getValue() as number;
          return <span>{value.toLocaleString()}</span>;
        },
      },
    ],
    [],
  );

  const table = useReactTable({
    columns,
    data: filteredData,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    onSortingChange: setSorting,
    state: {
      sorting,
    },
  });

  const handleProductDetail = (id: string) => {
    navigate(`/stock/detail/${id}`);
  };

  useEffect(() => {
    const handleGetProduct = async () => {
      if (!storeId) {
        return;
      }

      const parsedStoreId = parseInt(storeId, 10);
      if (isNaN(parsedStoreId)) {
        return;
      }

      try {
        const response = await getProducts(parsedStoreId);
        setData(response.data);
        setFilteredData(response.data);
      } catch (error) {
        console.error("Failed to fetch products:", error);
      }
    };

    handleGetProduct();
  }, [storeId]);

  const handleProductSearch = () => {
    const lowercasedTerm = searchTerm.toLowerCase();
    const filtered = data.filter(
      (product) =>
        product.name.toLowerCase().includes(lowercasedTerm) ||
        product.status.toLowerCase().includes(lowercasedTerm),
    );
    setFilteredData(filtered);
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-8 mt-12 flex w-full items-center justify-between">
          <BackButton />
          <h2 className="text-center text-2xl font-bold">재고 관리</h2>
          <button
            className="rounded border-2 p-2 hover:bg-base"
            onClick={() => navigate(`/product/create`, { state: storeId })}
          >
            <HiPlus />
          </button>
        </div>
        <div className="mb-4">
          <SearchBar
            placeholder="상품명 검색"
            value={searchTerm}
            onChange={(value) => setSearchTerm(value)}
            onSearch={handleProductSearch}
            maxLength={20}
          />
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-base">
              {table.getHeaderGroups().map((headerGroup) => (
                <tr key={headerGroup.id}>
                  {headerGroup.headers.map((header) => (
                    <th key={header.id} className="px-6 py-3 text-left">
                      {header.isPlaceholder ? null : (
                        <div
                          className={
                            header.column.getCanSort()
                              ? "flex cursor-pointer select-none flex-row items-center"
                              : ""
                          }
                          onClick={header.column.getToggleSortingHandler()}
                        >
                          {flexRender(
                            header.column.columnDef.header,
                            header.getContext(),
                          )}
                          {{
                            asc: <GoSortAsc className="ml-2" />,
                            desc: <GoSortDesc className="ml-2" />,
                          }[header.column.getIsSorted() as string] ?? null}
                        </div>
                      )}
                    </th>
                  ))}
                </tr>
              ))}
            </thead>
            <tbody>
              {table.getRowModel().rows.map((row) => (
                <tr
                  key={row.id}
                  className="border-b text-left"
                  onClick={() => handleProductDetail(row?.original?.id)}
                >
                  {row.getVisibleCells().map((cell) => (
                    <td key={cell.id} className="px-4 py-3">
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext(),
                      )}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  );
};

export default StockControl;
