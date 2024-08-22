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
  return (
    <div className="flex items-center space-x-6">
      <CiCircleMinus
        onClick={onDecrease}
        className="h-8 w-8 rounded-full text-primary hover:bg-secondary"
      />
      <span className="text-xl">{quantity}</span>
      <CiCirclePlus
        onClick={onIncrease}
        className="h-8 w-8 rounded-full text-primary hover:bg-secondary"
      />
    </div>
  );
};
