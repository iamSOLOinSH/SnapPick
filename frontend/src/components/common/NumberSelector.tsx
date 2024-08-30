import { CiCircleMinus, CiCirclePlus } from "react-icons/ci";

type NumberSelectorProps = {
  quantity: number;
  onIncrease: () => void;
  onDecrease: () => void;
};

export const NumberSelector: React.FC<NumberSelectorProps> = ({
  quantity,
  onIncrease,
  onDecrease,
}) => {
  const handleDecrease = () => {
    if (quantity > 1) {
      onDecrease();
    }
  };

  return (
    <div className="flex items-center space-x-6">
      <CiCircleMinus
        onClick={handleDecrease}
        className={`h-8 w-8 rounded-full text-primary hover:bg-secondary ${
          quantity <= 1 ? "cursor-not-allowed text-gray-300" : ""
        }`}
      />
      <span className="text-xl">{quantity}</span>
      <CiCirclePlus
        onClick={onIncrease}
        className="h-8 w-8 rounded-full text-primary hover:bg-secondary"
      />
    </div>
  );
};
