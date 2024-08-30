import { useState, useEffect } from "react";
import { InputLabel } from "../common/InputLabel";
import { Input } from "../common/Input";
import { SearchBar } from "../common/SearchBar";
import { Tag } from "../common/Tag";
import { useBoundStore } from "../../store/store";

interface StoreCreateStep3Props {
  setIsFormComplete: (isComplete: boolean) => void;
}

const StoreCreateStep3: React.FC<StoreCreateStep3Props> = ({
  setIsFormComplete,
}) => {
  const { tags, maxCapacity, addTag, removeTag, setMaxCapacity } =
    useBoundStore();

  const [tag, setTag] = useState("");
  const [tagError, setTagError] = useState("");

  const handleTagAdd = () => {
    setTagError("");

    if (tags.includes(tag)) {
      setTagError("같은 내용의 태그를 추가할 수 없습니다.");
      return;
    }

    if (tag) {
      addTag(tag);
      setTag("");
    }
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

  useEffect(() => {
    const isComplete = true;
    setIsFormComplete(isComplete);
  }, []);

  return (
    <div>
      <div className="mb-4">
        <InputLabel name="태그(다중 선택 가능)" />
        <SearchBar
          value={tag}
          onChange={(value) => setTag(value)}
          onSearch={handleTagAdd}
          maxLength={20}
        />
        {tagError && <p className="ml-4 mt-1 text-sm text-red">{tagError}</p>}

        {tags.length > 0 && (
          <div className="mt-2 flex flex-wrap">
            {tags.map((value: string) => (
              <Tag
                key={value}
                content={"#" + value}
                className="mb-1 mr-1"
                isDelete={true}
                onDelete={() => removeTag(value)}
              />
            ))}
          </div>
        )}
      </div>
      <div className="mb-4">
        <InputLabel name="최대 수용 가능 인원" />
        <Input
          name="최대 수용 가능 인원"
          value={maxCapacity}
          variant="half"
          onChange={(e) => handleNumberInput(e, setMaxCapacity)}
        />
      </div>
    </div>
  );
};

export default StoreCreateStep3;
