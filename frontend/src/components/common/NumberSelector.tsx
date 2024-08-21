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
      <button
        onClick={onDecrease}
        className="flex h-8 w-8 items-center justify-center rounded-full border-2 border-primary pb-1 text-2xl text-primary hover:bg-secondary"
      >
        -
      </button>
      <span className="text-xl">{quantity}</span>
      <button
        onClick={onIncrease}
        className="flex h-8 w-8 items-center justify-center rounded-full border-2 border-primary pb-1 text-2xl text-primary hover:bg-secondary"
      >
        +
      </button>
    </div>
  );
};
