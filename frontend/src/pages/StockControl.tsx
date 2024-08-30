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
import { useNavigate } from "react-router";
import { GoSortAsc, GoSortDesc } from "react-icons/go";

interface Product {
  id: string;
  name: string;
  quantity: number;
  status: "selling" | "soldout";
  productImg: string;
}

const StockControl = () => {
  const navigate = useNavigate();

  const [sorting, setSorting] = useState<SortingState>([]);
  const [data, setData] = useState<Product[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const PRODUCT_IMG =
    "https://www.nintendo.co.kr/character/kirby/assets/img/home/kirby-puffy.png";

  useEffect(() => {
    const exampleData: Product[] = [
      {
        id: "1",
        name: "스토어 물품 1",
        quantity: 5413,
        status: "selling",
        productImg: PRODUCT_IMG,
      },
      {
        id: "2",
        name: "스토어 물품 2",
        quantity: 21,
        status: "soldout",
        productImg: PRODUCT_IMG,
      },
      {
        id: "3",
        name: "스토어물품스토",
        quantity: 234,
        status: "selling",
        productImg: PRODUCT_IMG,
      },
      {
        id: "4",
        name: "스토어 물품 4",
        quantity: 234,
        status: "selling",
        productImg: PRODUCT_IMG,
      },
      {
        id: "5",
        name: "스토어 물품 5",
        quantity: 100,
        status: "selling",
        productImg: PRODUCT_IMG,
      },
      {
        id: "6",
        name: "스토어 물품 6",
        quantity: 0,
        status: "soldout",
        productImg: PRODUCT_IMG,
      },
      {
        id: "7",
        name: "스토어 물품 7",
        quantity: 0,
        status: "soldout",
        productImg: PRODUCT_IMG,
      },
    ];
    setData(exampleData);
  }, []);

  const columns = React.useMemo<ColumnDef<Product>[]>(
    () => [
      {
        accessorKey: "status",
        header: "상태",
        cell: (info) => {
          const status = info.getValue() as string;

          switch (status) {
            case "selling":
              return (
                <div className="flex flex-shrink-0">
                  <Tag content="판매중" variant="primary" />
                </div>
              );

            case "soldout":
              return <Tag content="품절" variant="red" />;
          }
        },
      },
      {
        accessorFn: (row) => ({ name: row.name, productImg: row.productImg }),
        id: "productInfo",
        header: "상품명",
        cell: (info) => {
          const { name, productImg } = info.getValue() as {
            name: string;
            productImg: string;
          };
          return (
            <div className="flex items-center text-left">
              {/* <div className="mr-2 h-10 w-10 flex-shrink-0 rounded-lg">
              <img
                src={productImg}
                alt="상품 이미지"
                className="h-full w-full rounded-lg object-cover"
              />
            </div> */}
              <span>{name}</span>
            </div>
          );
        },
      },

      {
        accessorKey: "quantity",
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
    data,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    onSortingChange: setSorting,
    state: {
      sorting,
    },
  });

  const handleProductSearch = () => {};

  const handleProductDetail = (id: string) => {
    navigate(`/stock/detail/${id}`);
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-8 mt-12 flex items-center justify-between">
          <div className="flex items-center">
            <BackButton />
            <h3 className="ml-4 text-2xl font-bold">재고 관리</h3>
          </div>
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
                  onClick={() => handleProductDetail(row.id)}
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
