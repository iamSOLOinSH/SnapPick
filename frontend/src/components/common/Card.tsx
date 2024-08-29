import { IoCloseSharp } from "react-icons/io5";

interface VariantStyles {
  container: string;
  image?: string;
  title: string;
  subtitle?: string;
  content?: string;
  description?: string;
  date?: string;
  price?: string;
  toggle?: string;
  spend?: string;
  totalPrice?: string;
}

const CARD_VARIANTS: Record<string, VariantStyles> = {
  status: {
    container:
      "mb-2 flex w-full flex-row items-center justify-between rounded-full bg-base p-4 shadow",
    title: "ml-2",
    content: "pr-2",
  },
  store: {
    container: "mb-2 flex bg-white p-4",
    image: "mr-4 h-32 w-32 rounded-lg",
    title: "text-lg font-medium text-black",
    subtitle: "text-sm text-gray-500",
    description: "mt-12 text-sm text-gray-500",
  },
  product: {
    container: "mb-2 flex justify-between rounded-sm bg-white p-4",
    image: "mr-4 h-32 w-32 rounded-lg",
    title: "text-medium text-lg ",
    price: "text-sm",
    totalPrice: "text-medium text-xl",
    toggle: "mt-1",
  },
  simple: {
    container: "mb-2 flex items-center rounded-lg p-4 bg-base mx-4",
    image: "mr-4 h-12 w-12 rounded-lg",
    title: "text-lg font-medium text-black",
    date: "text-sm text-gray-500",
    spend: "text-xl font-medium text-black",
  },
  mini: {
    container: "mr-1 inline-block w-32 truncate",
    image: "h-32 w-32 rounded-lg",
    title: "mt-1 pl-0.5 text-sm",
  },
  big: {
    container: "mr-1 inline-block w-full truncate",
    image: "h-full w-full rounded-lg",
    title: "mt-1 pl-0.5 text-sm",
  },
} as const;

type CardProps = {
  variant: keyof typeof CARD_VARIANTS;
  imageSrc?: string;
  title: string;
  subtitle?: string;
  description?: string;
  price?: number;
  totalPrice?: number;
  date?: string;
  time?: string;
  content?: React.ReactElement;
  toggle?: React.ReactElement;
  quantity?: number;
  spend?: number;
};

export const Card: React.FC<CardProps> = ({
  variant,
  imageSrc,
  title,
  subtitle,
  description,
  price,
  date,
  time,
  content,
  toggle,
  spend,
  totalPrice,
}) => {
  const styles = CARD_VARIANTS[variant];

  return (
    <div className={styles.container}>
      {imageSrc && <img src={imageSrc} alt={title} className={styles.image} />}

      <div className="flex-grow">
        <div className="flex items-center justify-between">
          <div>
            <h3 className={styles.title}>{title}</h3>
            {(date || time) && (
              <div className={styles.date}>
                {date}
                <span className="ml-2">{time}</span>
              </div>
            )}
          </div>
          {spend && (
            <div className="text-right">
              <p className={styles.spend}>-{spend.toLocaleString()}원</p>
            </div>
          )}
          {price && (
            <IoCloseSharp className="h-6 w-6 rounded-full text-gray-500 hover:bg-secondary" />
          )}
        </div>
        {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
        {price && (
          <p className={styles.price}>개당 ￦{price.toLocaleString()}</p>
        )}
        {description && <p className={styles.description}>{description}</p>}
        {totalPrice && (
          <div className="mt-6 text-right">
            <p className={styles.totalPrice}>{totalPrice.toLocaleString()}원</p>
          </div>
        )}
        {toggle && (
          <div className="flex justify-end">
            <div className={styles.toggle}>{toggle}</div>
          </div>
        )}
      </div>

      {content && <div className={styles.content}>{content}</div>}
    </div>
  );
};
