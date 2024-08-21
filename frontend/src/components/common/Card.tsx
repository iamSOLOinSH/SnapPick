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
}

const CARD_VARIANTS: Record<string, VariantStyles> = {
  status: {
    container:
      "p-4 mb-2 w-full bg-base rounded-full shadow flex flex-row justify-between items-center",
    title: "ml-2",
    content: "pr-2",
  },
  store: {
    container: "p-4 mb-2 bg-white flex",
    image: "w-32 h-32 rounded-lg bg-gray-200 mr-4",
    title: "text-lg font-medium text-black",
    subtitle: "text-gray-500 text-sm",
    description: "text-gray-500 text-sm mt-12",
  },
  product: {
    container: "p-4 mb-2 bg-white rounded-sm flex justify-between",
    image: "w-32 h-32 rounded-lg bg-gray-200 mr-4",
    title: "text-lg font-medium",
    subtitle: "text-sm",
    price: "text-xl text-medium",
    toggle: "mt-1",
  },
  simple: {
    container: "p-4 mb-2 rounded-lg flex items-center",
    image: "w-12 h-12 rounded-lg bg-gray-200 mr-4",
    title: "text-lg font-medium text-black",
    date: "text-gray-500 text-sm",
    spend: "text-xl font-medium text-black",
  },
} as const;

type CardProps = {
  variant: keyof typeof CARD_VARIANTS;
  imageSrc?: string;
  title: string;
  subtitle?: string;
  description?: string;
  price?: string;
  date?: string;
  time?: string;
  content?: React.ReactElement;
  toggle?: React.ReactElement;
  quantity?: number;
  spend?: string;
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
              <p className={styles.spend}>{spend}</p>
            </div>
          )}
        </div>
        {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
        {description && <p className={styles.description}>{description}</p>}
        {price && (
          <div className="mt-6 text-right">
            <p className={styles.price}>{price}</p>
          </div>
        )}
        {toggle && (
          <div className="flex justify-end">
            <p className={styles.toggle}>{toggle}</p>
          </div>
        )}
      </div>

      {content && <div className={styles.content}>{content}</div>}
    </div>
  );
};
