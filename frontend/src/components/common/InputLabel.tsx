export const InputLabel = ({ name }: { name: string }) => {
  return (
    <label
      htmlFor={name}
      className="text-md my-2 ml-4 block font-semibold text-gray-800"
    >
      {name}
    </label>
  );
};
