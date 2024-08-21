import { useState } from "react";

import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { Input } from "../components/common/Input";
import { Card } from "../components/common/Card";
import { NumberSelector } from "../components/common/NumberSelector";

const Main = () => {
  const [quantity, setQuantity] = useState(3);

  const handleIncrease = () => setQuantity(quantity + 1);
  const handleDecrease = () => setQuantity(quantity - 1);
  return (
    <Layout>
      {/* <Search /> */}
      <h2>안녕 😃</h2>
      <h3>넌 최고야👍</h3>
      <br />
      <input type="text" placeholder="너의 꿈은 뭐야?" />
      <br />
      <div className="mb-4">멋있어👏</div>
      <Button variant="primary" content="다음" />
      <div className="my-2" />
      <Button variant="secondary" content="취소" />
      <div className="my-2" />
      <Input variant="full" placeholder="스토어 이름(최대 50자)" />
      <div className="my-2" />
      <div>
        <Input variant="half" placeholder="수량" /> <span> </span>
        <Input variant="half" placeholder="수량" />
      </div>
      <div className="my-2" />
      <div>
        <Input variant="third" placeholder="010" /> <span> - </span>
        <Input variant="third" placeholder="010" /> <span> - </span>
        <Input variant="third" placeholder="010" />
      </div>
      <div className="my-2" />
      <div>
        <span>월 </span>
        <Input variant="twoThirds" placeholder="09:00 - 18:00" />
      </div>
      <div className="my-2" />
      <div className="align-center flex">
        <Input variant="check" type="checkbox" />
        <span className="ml-2">해당 없음</span>
      </div>
      <div className="my-2" />
      <Card
        variant="status"
        title="상태"
        content={
          <div className="flex items-center gap-4">
            <div className="h-3 w-3 rounded-full bg-red-500"></div>
            <span>품절</span>
            <span>?</span>
          </div>
        }
      />
      <Card variant="status" title="옵션" content={<div>상품 옵션 1</div>} />
      <Card
        variant="store"
        title="팝업 스토어 1"
        subtitle="팝업스토어 소개"
        description="운영시간 10:30-20:00"
        imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
      />
      <Card
        variant="product"
        title="스토어 상품 1"
        subtitle="개당 ￦16,000"
        price={`${quantity * 16000}원`}
        imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300"
        toggle={
          <NumberSelector
            quantity={quantity}
            onIncrease={handleIncrease}
            onDecrease={handleDecrease}
          />
        }
      />
      <Card
        variant="simple"
        title="팝업 스토어 1"
        date="2024년 8월 17일"
        time="15:02"
        imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
        spend="-156,000원"
      />
      <Card
        variant="simple"
        title="팝업 스토어 1"
        date="2024년 8월 17일"
        time="15:02"
        imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
      />
    </Layout>
  );
};

export default Main;
